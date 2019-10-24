#
# system.prop for msm8953-common
#

#COSMIC-OS
ro.cos.maintainer=SCISSORDRAGONBOY

# ADB at boot
persist.service.adb.enable=1
persist.service.debuggable=1
persist.sys.usb.config=mtp,adb
ro.adb.secure=0

# Audio
PRODUCT_PROPERTY_OVERRIDES += \
  af.fast_track_multiplier=2 \
  audio.deep_buffer.media=true \
  audio.offload.disable=true \
  audio.offload.min.duration.secs=30 \
  audio.offload.video=true \
  persist.vendor.audio.fluence.speaker=true \
  persist.vendor.audio.fluence.voicecall=true \
  persist.vendor.audio.fluence.voicerec=true \
  persist.vendor.btstack.enable.splita2dp=false \
  persist.vendor.audio.hw.binder.size_kbyte=1024 \
  ro.vendor.audio.sdk.fluencetype=fluence \
  ro.af.client_heap_size_kbyte=7168 \
  ro.vendor.audio.sdk.ssr=false \
  vendor.audio.flac.sw.decoder.24bit=true \
  vendor.audio.offload.buffer.size.kb=64 \
  vendor.audio.offload.gapless.enabled=true \
  vendor.audio.offload.multiaac.enable=true \
  vendor.audio.offload.multiple.enabled=false \
  vendor.audio.offload.track.enable=true \
  vendor.audio.parser.ip.buffer.size=0 \
  vendor.audio.playback.mch.downsample=true \
  vendor.audio.pp.asphere.enabled=false \
  vendor.audio.safx.pbe.enabled=true \
  vendor.audio.tunnel.encode=false \
  vendor.audio.use.sw.alac.decoder=true \
  vendor.audio.use.sw.ape.decoder=true \
  vendor.audio_hal.period_size=192 \
  vendor.voice.conc.fallbackpath=deep-buffer \
  audio.dolby.ds2.hardbypass=true \
  vendor.audio.dolby.ds2.enabled=true \
  vendor.voice.path.for.pcm.voip=true \
  vendor.voice.playback.conc.disabled=true \
  vendor.voice.record.conc.disabled=false \
  ro.config.media_vol_steps=25 \
  ro.config.vc_call_vol_steps=7 \
  persist.audio.dirac.speaker=true \
  vendor.voice.voip.conc.disabled=true

# Bluetooth
PRODUCT_PROPERTY_OVERRIDES += \
  bluetooth.hfp.client=1 \
  persist.vendor.bt.a2dp_offload_cap=sbc-aptx-aptxtws-aptxhd-aac-ldac \
  persist.vendor.bt.enable.splita2dp=true \
  qcom.bluetooth.soc=smd \
  ro.bluetooth.hfp.ver=1.7 \
  ro.qualcomm.bt.hci_transport=smd

# Camera
PRODUCT_PROPERTY_OVERRIDES += \
  camera.display.lmax=1280x720 \
  camera.display.umax=1920x1080 \
  camera.hal1.packagelist=com.skype.raider,com.google.android.talk \
  camera.lowpower.record.enable=1 \
  media.camera.ts.monotonic=1 \
  persist.camera.gyro.disable=0 \
  persist.camera.isp.clock.optmz=0 \
  persist.camera.stats.test=5 \
  persist.camera.eis.enable=1 \
  persist.vendor.qti.telephony.vt_cam_interface=1 \
  vidc.enc.dcvs.extra-buff-count=2 \
  persist.camera.HAL3.enabled=1 \
  vendor.camera.hal1.packagelist=com.whatsapp \
  vendor.camera.aux.packagelist=com.android.camera,com.google.android.Pixel3Mod

#Expose aux camera for below packages
PRODUCT_PROPERTY_OVERRIDES += \
    vendor.camera.hal1.packagelist=com.whatsapp \
    vendor.camera.aux.packagelist=org.codeaurora.snapcam,com.android.camera,com.qualcomm.qti.qmmi \
    camera.hal1.packagelist=com.whatsapp,com.facebook.katana,com.instagram.android,com.snapchat.android

#disable UBWC for camera
PRODUCT_PROPERTY_OVERRIDES += \
    persist.camera.preview.ubwc=0 \
    persist.camera.stats.test=0 \
    persist.camera.depth.focus.cb=0 \
    persist.camera.isp.clock.optmz=0 \
    persist.camera.hist.high=20 \
    persist.camera.hist.drc=1.2 \
    persist.camera.linkpreview=0 \
    persist.camera.isp.turbo=1

# Cne/Dpm
PRODUCT_PROPERTY_OVERRIDES += \
  persist.cne.feature=1 \
  persist.dpm.feature=1

# Coresight
  PRODUCT_PROPERTY_OVERRIDES += \
  persist.debug.coresight.config=stm-events

# Display
PRODUCT_PROPERTY_OVERRIDES += \
 debug.egl.hw=0 \
 debug.enable.sglscale=1 \
 debug.gralloc.enable_fb_ubwc=1 \
 debug.mdpcomp.logs=0 \
 debug.sf.hw=0 \
 dev.pm.dyn_samplingrate=1 \
 persist.debug.wfd.enable=1 \
 persist.demo.hdmirotationlock=false \
 vendor.display.disable_rotator_downscale=1 \
 persist.hwc.enable_vds=1 \
 persist.hwc.mdpcomp.enable=true \
 ro.opengles.version=196610 \
 ro.qualcomm.cabl=0 \
 ro.vendor.display.cabl=2 \
 ro.sf.lcd_density=320 \
 persist.sys.wfd.nohdcp=1 \
 persist.debug.wfd.enable=1 \
 persist.sys.wfd.virtual=0 

# Fp Gestures
PRODUCT_PROPERTY_OVERRIDES += \
    persist.sys.fp.navigation=1

# DRM
PRODUCT_PROPERTY_OVERRIDES += \
 drm.service.enabled=true

# Fingerprint
PRODUCT_PROPERTY_OVERRIDES += \
 persist.qfp=false \
 ro.fingerprint.cleanup.unused=false

# Fm
PRODUCT_PROPERTY_OVERRIDES += \
 ro.fm.transmitter=false

# Frp
PRODUCT_PROPERTY_OVERRIDES += \
 ro.frp.pst=/dev/block/bootdevice/by-name/config

# GPS
PRODUCT_PROPERTY_OVERRIDES += \
 persist.gps.qc_nlp_in_use=1 \
 persist.loc.nlp_name=com.qualcomm.location \
 ro.gps.agps_provider=1

# Media
PRODUCT_PROPERTY_OVERRIDES += \
 av.debug.disable.pers.cache=1 \
 media.aac_51_output_enabled=true \
 media.msm8956hw=0 \
 media.stagefright.audio.sink=280 \
 mm.enable.qcom_parser=1048575 \
 mm.enable.sec.smoothstreaming=true \
 mmp.enable.3g2=true \
 vendor.audio.hw.aac.encoder=true \
 vendor.vidc.dec.downscalar_height=1088 \
 vendor.vidc.dec.downscalar_width=1920 \
 vendor.vidc.disable.split.mode=1 \
 vendor.vidc.enc.disable.pq=true \
 vendor.vidc.enc.disable_bframes=1 \
 debug.sf.enable_hwc_vds=1 \
 persist.sys.wfd.virtual=0


# Perf
PRODUCT_PROPERTY_OVERRIDES += \
 ro.sys.fw.dex2oat_thread_count=4 \
 ro.vendor.extension_library=libqti-perfd-client.so

# Netmgrd
PRODUCT_PROPERTY_OVERRIDES += \
 ro.use_data_netmgrd=true \
 persist.data.netmgrd.qos.enable=true \
 persist.data.mode=concurrent

# Nitz
PRODUCT_PROPERTY_OVERRIDES += \
 persist.rild.nitz_plmn="" \
 persist.rild.nitz_long_ons_0="" \
 persist.rild.nitz_long_ons_1="" \
 persist.rild.nitz_long_ons_2="" \
 persist.rild.nitz_long_ons_3="" \
 persist.rild.nitz_short_ons_0="" \
 persist.rild.nitz_short_ons_1="" \
 persist.rild.nitz_short_ons_2="" \
 persist.rild.nitz_short_ons_3=""

# Radio
PRODUCT_PROPERTY_OVERRIDES += \
 DEVICE_PROVISIONED=1 \
 persist.data.iwlan.enable=true \
 persist.dbg.ims_volte_enable=1 \
 persist.dbg.volte_avail_ovr=1 \
 persist.dbg.vt_avail_ovr=1 \
 persist.dbg.wfc_avail_ovr=0 \
 persist.vendor.radio.apm_sim_not_pwdn=1 \
 persist.radio.calls.on.ims=0 \
 persist.radio.csvt.enabled=false \
 persist.vendor.radio.hw_mbn_update=0 \
 persist.radio.jbims=0 \
 persist.radio.mt_sms_ack=20 \
 persist.radio.multisim.config=dsds \
 persist.vendor.sw_mbn_update=0 \
 persist.radio.videopause.mode=1 \
 persist.vendor.radio.custom_ecc=1 \
 persist.vendor.radio.rat_on=combine \
 persist.vendor.radio.sib16_support=1 \
 ril.subscription.types=NV,RUIM \
 rild.libargs=-d/dev/smd0 \
 rild.libpath=/vendor/lib64/libril-qc-qmi-1.so \
 vendor.rild.libpath=/vendor/lib64/libril-qc-qmi-1.so \
 ro.telephony.call_ring.multiple=false \
 ro.telephony.default_network=22,22 \
 service.qti.ims.enabled=1 \
 telephony.lteOnCdmaDevice=1

PRODUCT_PROPERTY_OVERRIDES += \
debug.sf.early_phase_offset_ns=1500000 \
debug.sf.early_app_phase_offset_ns=1500000 \
debug.sf.early_gl_phase_offset_ns=3000000 \
debug.sf.early_gl_app_phase_offset_ns=15000000
    
# Time Services
PRODUCT_PROPERTY_OVERRIDES += \
 persist.timed.enable=true

# Tcp
PRODUCT_PROPERTY_OVERRIDES += \
 net.tcp.2g_init_rwnd=10

# Usb
PRODUCT_PROPERTY_OVERRIDES += \
 persist.sys.usb.config.extra=none

# IMS / VoLTE
PRODUCT_PROPERTY_OVERRIDES += \
 persist.dbg.volte_avail_ovr=1 \
 persist.dbg.vt_avail_ovr=1 \
 persist.dbg.wfc_avail_ovr=1 \
 persist.radio.lte_vrte_ltd=1 \
 persist.radio.VT_CAM_INTERFACE=2 \
 persist.radio.VT_ENABLE=1 \
 persist.radio.VT_HYBRID_ENABLE=1 \
 persist.volte_enabled_by_hw=1

# SurfaceFlinger
PRODUCT_DEFAULT_PROPERTY_OVERRIDES += \
    ro.surface_flinger.protected_contents=true

# Optimize
 PRODUCT_PROPERTY_OVERRIDES += \
     sys.use_fifo_ui=1

# DPM
#PRODUCT_PROPERTY_OVERRIDES += \
    persist.vendor.dpm.feature=5

# Wifi
PRODUCT_PROPERTY_OVERRIDES += \
 wifi.interface=wlan0

#low audio flinger standby delay to reduce power consumption
  PRODUCT_PROPERTY_OVERRIDES += \
  ro.audio.flinger_standbytime_ms=300
