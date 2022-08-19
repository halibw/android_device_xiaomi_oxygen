#!/bin/bash
#
# Copyright (C) 2016 The CyanogenMod Project
# Copyright (C) 2017-2020 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

function blob_fixup() {
    case "${1}" in
vendor/lib64/libgf_hal.so)
            # Always assume screen is interactive
            sed -i -e 's|\xE6\xDB\xFF\x97\x60\x02\x00\xB9|\x20\x00\x80\xD2\x20\x00\x80\xD2|g' "${2}"
            PATTERN_FOUND=$(hexdump -ve '1/1 "%.2x"' "${2}" | grep -E -o "200080d2200080d2" | wc -l)
            if [ $PATTERN_FOUND != "1" ]; then
                echo "Critical blob modification weren't applied on ${2}!"
                exit;
            fi
            sed -i -e 's|\x91\xD2\xFF\x97\x3C\xD3\xFF\x97|\x91\xD2\xFF\x97\x20\x00\x80\x52|g' "${2}"
            PATTERN_FOUND=$(hexdump -ve '1/1 "%.2x"' "${2}" | grep -E -o "91d2ff9720008052" | wc -l)
            if [ $PATTERN_FOUND != "1" ]; then
                echo "Critical blob modification weren't applied on ${2}!"
                exit;
            fi
	    ;;
# Use VNDK 32 libhidlbase
vendor/lib64/libvendor.goodix.hardware.fingerprint@1.0.so)
            "${PATCHELF_0_8}" --remove-needed "libhidlbase.so" "${2}"
            sed -i "s/libhidltransport.so/libhidlbase-v32.so\x00/" "${2}"
            ;;
    esac
}

set -e

DEVICE=oxygen
VENDOR=xiaomi

# Load extract_utils and do some sanity checks
MY_DIR="${BASH_SOURCE%/*}"
if [[ ! -d "${MY_DIR}" ]]; then MY_DIR="${PWD}"; fi

ANDROID_ROOT="${MY_DIR}/../../.."

HELPER="${ANDROID_ROOT}/tools/extract-utils/extract_utils.sh"
if [ ! -f "$HELPER" ]; then
    echo "Unable to find helper script at $HELPER"
    exit 1
fi
source "${HELPER}"

# Default to sanitizing the vendor folder before extraction
CLEAN_VENDOR=true

KANG=
SECTION=

while [ "${#}" -gt 0 ]; do
    case "${1}" in
        -n | --no-cleanup )
                CLEAN_VENDOR=false
                ;;
        -k | --kang )
                KANG="--kang"
                ;;
        -s | --section )
                SECTION="${2}"; shift
                CLEAN_VENDOR=false
                ;;
        * )
                SRC="${1}"
                ;;
    esac
    shift
done

if [ -z "${SRC}" ]; then
    SRC="adb"
fi

# Initialize the helper
setup_vendor "${DEVICE}" "${VENDOR}" "${ANDROID_ROOT}" false "${CLEAN_VENDOR}"

extract "${MY_DIR}"/proprietary-files.txt "${SRC}" \
        "${KANG}" --section "${SECTION}"

DEVICE_BLOB_ROOT="${ANDROID_ROOT}"/vendor/"${VENDOR}"/"${DEVICE}"/proprietary

# Camera configs
sed -i "s|/system/etc/camera|/vendor/etc/camera|g" "${DEVICE_BLOB_ROOT}"/vendor/lib/libmmcamera2_sensor_modules.so

# Camera socket
sed -i "s|/data/misc/camera/cam_socket|/data/vendor/qcam/cam_socket|g" "${DEVICE_BLOB_ROOT}"/vendor/bin/mm-qcamera-daemon

# Camera data
for CAMERA_LIB in libmmcamera2_cpp_module.so libmmcamera2_dcrf.so libmmcamera2_iface_modules.so libmmcamera2_imglib_modules.so libmmcamera2_mct.so libmmcamera2_pproc_modules.so libmmcamera2_q3a_core.so libmmcamera2_sensor_modules.so libmmcamera2_stats_algorithm.so libmmcamera2_stats_modules.so libmmcamera_dbg.so libmmcamera_imglib.so libmmcamera_pdafcamif.so libmmcamera_pdaf.so libmmcamera_tintless_algo.so libmmcamera_tintless_bg_pca_algo.so libmmcamera_tuning.so; do
    sed -i "s|/data/misc/camera/|/data/vendor/qcam/|g" "${DEVICE_BLOB_ROOT}"/vendor/lib/${CAMERA_LIB}
done

# Camera debug log file
sed -i "s|persist.camera.debug.logfile|persist.vendor.camera.dbglog|g" "${DEVICE_BLOB_ROOT}"/vendor/lib/libmmcamera_dbg.so

# Camera graphicbuffer shim
"${PATCHELF}" --add-needed "libshims_ui.so" "${DEVICE_BLOB_ROOT}"/vendor/lib/libmmcamera_ppeiscore.so

# Camera VNDK support
"${PATCHELF}" --replace-needed "libandroid.so" "libshims_android.so" "${DEVICE_BLOB_ROOT}"/vendor/lib/libmmcamera2_stats_modules.so
sed -i "s|libgui.so|libwui.so|g" "${DEVICE_BLOB_ROOT}"/vendor/lib/libmmcamera2_stats_modules.so
sed -i "s|libgui.so|libwui.so|g" "${DEVICE_BLOB_ROOT}"/vendor/lib/libmmcamera_ppeiscore.so
"${PATCHELF}" --remove-needed "libandroid.so" "${DEVICE_BLOB_ROOT}"/vendor/lib/libmpbase.so

# FPC
${PATCHELF}" --add-needed "libshims_binder.so" "${DEVICE_BLOB_ROOT}"/vendor/lib64/lib_fpc_tac_shared.so

# Goodix
"${PATCHELF}" --remove-needed "libprotobuf-cpp-lite.so" "${DEVICE_BLOB_ROOT}"/vendor/lib64/libvendor.goodix.hardware.fingerprint@1.0.so
"${PATCHELF}" --remove-needed "libprotobuf-cpp-lite.so" "${DEVICE_BLOB_ROOT}"/vendor/lib64/libvendor.goodix.hardware.fingerprint@1.0-service.so

# IMS
"${PATCHELF}" --add-needed "libims-shim.so" "${DEVICE_BLOB_ROOT}"/system_ext/lib64/lib-imsvideocodec.so

"${MY_DIR}/setup-makefiles.sh"
