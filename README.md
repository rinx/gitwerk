# gitwerk

[![LICENSE](https://img.shields.io/github/license/rinx/gitwerk)](https://github.com/rinx/gitwerk/blob/master/LICENSE)
[![release](https://img.shields.io/github/v/release/rinx/gitwerk)](https://github.com/rinx/gitwerk/releases)
[![GitHub Actions: Run gitwerk](https://github.com/rinx/gitwerk/workflows/Run%20gitwerk/badge.svg)](https://github.com/rinx/gitwerk/actions)
[![GitHub Actions: Build Docker image](https://github.com/rinx/gitwerk/workflows/Build%20docker%20image/badge.svg)](https://github.com/rinx/gitwerk/actions)
[![GitHub Actions: Build Native image](https://github.com/rinx/gitwerk/workflows/Build%20native%20image/badge.svg)](https://github.com/rinx/gitwerk/actions)
[![GitHub Actions: Build Native image for release](https://github.com/rinx/gitwerk/workflows/Build%20native%20image%20for%20release/badge.svg)](https://github.com/rinx/gitwerk/actions)

## Usage

### semver-auto

increments version by git log message contexts

```
## when the latest tag is v0.0.1
## the commit comment includes "[patch]"

$ gitwerk semver-auto
## increments tag to v0.0.2
```

### semver

prints incremented version.

```
## when the latest tag is v0.0.1

$ gitwerk semver patch
v0.0.2

$ gitwerk semver minor
v0.1.0

$ gitwerk semver major
v1.0.0
```
