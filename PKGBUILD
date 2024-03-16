pkgname=psychosis-studio-git
pkgver=0.1.0
pkgrel=1
pkgdesc="A SELinux policy editor"
arch=('any')
url="https://github.students.cs.ubc.ca/CPSC210-2023W-T2/project_x6a8w"
license=('MIT')
depends=('java-runtime')
makedepends=('git' 'gradle')
#source=("src", "data")
#md5sums=('SKIP')

build() {
  cd ..
  gradle build
  tar -xvf "build/distributions/psychosis.tar"
}

package() { 
  cd ..
  install -Dm755 psychosis/bin/psychosis "$pkgdir/opt/psychosis"
#  install -dm755 "$pkgdir/usr/share/java/$pkgname"
#  install -m644 build/libs/*.jar "$pkgdir/usr/share/java/$pkgname"
#  install -Dm755 "$srcdir/$pkgname/src/main/ui/Main" "$pkgdir/usr/bin/$pkgname"
}
