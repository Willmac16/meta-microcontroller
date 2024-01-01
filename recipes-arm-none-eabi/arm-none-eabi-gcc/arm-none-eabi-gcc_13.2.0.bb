SUMMARY = "GCC for ARM embedded controllers (arm-none-eabi)"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM="\
    file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
    file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1\
    file://COPYING3;md5=d32239bcb673463ab874e80d47fae504\
    file://COPYING3.LIB;md5=6a6a8e020838b23406c81b19c1d46df6\
    file://include/COPYING;md5=59530bdf33659b29e73d4adb9f9f6552\
    file://include/COPYING3;md5=d32239bcb673463ab874e80d47fae504\
    file://libiberty/COPYING.LIB;md5=a916467b91076e631dd8edb7424769c7\
"

inherit autotools gettext texinfo

gmp='gmp-6.2.1.tar.bz2'
mpfr='mpfr-4.1.0.tar.bz2'
mpc='mpc-1.2.1.tar.gz'
isl='isl-0.24.tar.bz2'

infra_base_url='http://gcc.gnu.org/pub/gcc/infrastructure/'

SRC_URI = "\
  ftp://ftp.gnu.org/gnu/gcc/gcc-${PV}/gcc-${PV}.tar.gz;name=gcc \
  ${infra_base_url}${gmp};name=gmp \
  ${infra_base_url}${mpfr};name=mpfr \
  ${infra_base_url}${mpc};name=mpc \
  ${infra_base_url}${isl};name=isl \
"

SRC_URI[gcc.md5sum] = "aeb5ac806c34d47e725bdd025f34bac4"
SRC_URI[gcc.sha256sum] = "8cb4be3796651976f94b9356fa08d833524f62420d6292c5033a9a26af315078"

SRC_URI[gmp.md5sum] = "28971fc21cf028042d4897f02fd355ea"
SRC_URI[gmp.sha256sum] = "eae9326beb4158c386e39a356818031bd28f3124cf915f8c5b1dc4c7a36b4d7c"

SRC_URI[mpfr.md5sum] = "44b892bc5a45bafb4294d134e13aad1d"
SRC_URI[mpfr.sha256sum] = "feced2d430dd5a97805fa289fed3fc8ff2b094c02d05287fd6133e7f1f0ec926"

SRC_URI[mpc.md5sum] = "9f16c976c25bb0f76b50be749cd7a3a8"
SRC_URI[mpc.sha256sum] = "17503d2c395dfcf106b622dc142683c1199431d095367c6aacba6eec30340459"

SRC_URI[isl.md5sum] = "dd2f7b78e118c25bd96134a52aae7f4d"
SRC_URI[isl.sha256sum] = "fcf78dd9656c10eb8cf9fbd5f59a0b6b01386205fe1934b3b287a0a1898145c0"

S = "${WORKDIR}/gcc-${PV}"

BBCLASSEXTEND = "native"

TARGET = "arm-none-eabi"
PREFIX = "/opt/gnuarm"

EXTRA_OECONF = " \
  --target=$TARGET \
  --prefix=$PREFIX \
  --without-headers \
  --with-newlib \
  --with-gnu-as \
  --with-gnu-ld \
  --enable-languages=c \
  --enable-frame-pointer=no \
"

do_patch () {
    # Move the external deps into the source dir
    mv ${WORKDIR}/$(echo ${gmp} | sed 's/\.tar\.bz2$//') ${S}/gmp
    mv ${WORKDIR}/$(echo ${mpfr} | sed 's/\.tar\.bz2$//') ${S}/mpfr
    mv ${WORKDIR}/$(echo ${mpc} | sed 's/\.tar\.gz$//') ${S}/mpc
    mv ${WORKDIR}/$(echo ${isl} | sed 's/\.tar\.bz2$//') ${S}/isl
}

do_configure () {
    (cd ${S} && gnu-configize)
    oe_runconf
}

do_compile () {
    oe_runmake all-gcc
}

do_install () {
    oe_runmake install-gcc DESTDIR=${D}
}

# do_install:append() {
#     # remove some files conflicting with target utils
#     rm -rf ${D}/${datadir}/locale
#     rm -rf ${D}/${datadir}/info
#     rm -rf ${D}/${libdir}/bfd-plugins
#     rmdir ${D}/${libdir}
# }

FILES:${PN} += "${prefix}/arm-none-eabi"
SYSROOT_DIRS:append:class-native = " ${prefix}/arm-none-eabi"
