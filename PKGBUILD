pkgname=psychosis-studio-git
pkgver=0.1.0
pkgrel=1
pkgdesc="A SELinux policy editor"
arch=('any')
url="https://github.students.cs.ubc.ca/CPSC210-2023W-T2/project_x6a8w"
license=('MIT')
depends=('java-runtime')
makedepends=('git' 'gradle')
source=("git@github.students.cs.ubc.ca:CPSC210-2023W-T2/project_x6a8w.git")
md5sums=('SKIP')

prepare() {
  cd "$pkgname"
}

build() {
  cd "$pkgname"
  ./gradlew build
}

package() {
  cd "$pkgname"
  install -dm755 "$pkgdir/usr/share/java/$pkgname"
  install -m644 build/libs/*.jar "$pkgdir/usr/share/java/$pkgname"
  install -Dm755 "$srcdir/$pkgname/src/main/ui/Main" "$pkgdir/usr/bin/$pkgname"
}
