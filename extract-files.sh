  
#!/bin/bash
#
# Copyright (C) 2016 The CyanogenMod Project
# Copyright (C) 2017-2020 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

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
#sed -i "s|/system/etc/camera|/vendor/etc/camera|g" "${DEVICE_BLOB_ROOT}"/vendor/lib/libmmcamera2_sensor_modules.so

# Camera socket
#sed -i "s|/data/misc/camera/cam_socket|/data/vendor/qcam/cam_socket|g" "${DEVICE_BLOB_ROOT}"/vendor/bin/mm-qcamera-daemon

# Camera VNDK support
#"${PATCHELF}" --remove-needed "libandroid.so" "${DEVICE_BLOB_ROOT}"/vendor/lib/libmmcamera2_stats_modules.so
#"${PATCHELF}" --remove-needed "libgui.so" "${DEVICE_BLOB_ROOT}"/vendor/lib/libmmcamera2_stats_modules.so
#sed -i "s|libandroid.so|libcamshim.so|g" "${DEVICE_BLOB_ROOT}"/vendor/lib/libmmcamera2_stats_modules.so
#"${PATCHELF}" --remove-needed "libgui.so" "${DEVICE_BLOB_ROOT}"/vendor/lib/libmmcamera_ppeiscore.so
#"${PATCHELF}" --remove-needed "libandroid.so" "${DEVICE_BLOB_ROOT}"/vendor/lib/libmpbase.so

# Goodix
"${PATCHELF}" --remove-needed "libunwind.so" "${DEVICE_BLOB_ROOT}"/vendor/bin/goodixfp
"${PATCHELF}" --remove-needed "libbacktrace.so" "${DEVICE_BLOB_ROOT}"/vendor/bin/goodixfp
"${PATCHELF}" --add-needed "libbinder_gdxfp.so" "${DEVICE_BLOB_ROOT}"/vendor/bin/goodixfp
"${PATCHELF}" --add-needed "fakelogprint.so" "${DEVICE_BLOB_ROOT}"/vendor/bin/goodixfp
"${PATCHELF}" --add-needed "fakelogprint.so" "${DEVICE_BLOB_ROOT}"/vendor/lib64/hw/fingerprint.goodix.msm8953.so
"${PATCHELF}" --add-needed "fakelogprint.so" "${DEVICE_BLOB_ROOT}"/vendor/lib64/hw/fingerprint.goodix.so

"${MY_DIR}/setup-makefiles.sh"
