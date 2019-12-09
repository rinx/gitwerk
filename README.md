# gitwerk

[![LICENSE](https://img.shields.io/github/license/rinx/gitwerk)](https://github.com/rinx/gitwerk/blob/master/LICENSE)
[![release](https://img.shields.io/github/v/release/rinx/gitwerk)](https://github.com/rinx/gitwerk/releases)
[![Docker Pulls](https://img.shields.io/docker/pulls/rinx/gitwerk.svg?style=flat-square)](https://hub.docker.com/r/rinx/gitwerk)
[![GitHub Actions: Run gitwerk](https://github.com/rinx/gitwerk/workflows/Run%20gitwerk/badge.svg)](https://github.com/rinx/gitwerk/actions)
[![GitHub Actions: Build Docker image](https://github.com/rinx/gitwerk/workflows/Build%20docker%20image/badge.svg)](https://github.com/rinx/gitwerk/actions)
[![GitHub Actions: Build Native image](https://github.com/rinx/gitwerk/workflows/Build%20native%20image/badge.svg)](https://github.com/rinx/gitwerk/actions)

## Usage

### semver-auto

increments version by git log message contexts

```
## when the latest tag is v0.0.1
$ git commit -m "[patch] increment patch version"
## the commit comment contains "[patch]"

$ gitwerk semver-auto
## increments tag to v0.0.2

###...

$ git commit -m "[tag.suffix=-alpha] [minor] increment minor version and add suffix"
## the commit comment contains "[minor]" and "[tag.suffix=-alpha]"

$ gitwerk semver-auto
## increments tag to v0.1.0-alpha

###...

$ git commit -m "[tag.prefix=] [tag.suffix=] [major] increment major version and remove prefix and suffix"
## the commit comment contains "[major]", "[tag.prefix=]" and "[tag.suffix=]"

$ gitwerk semver-auto
## increments tag to 1.0.0

###...

$ git commit -m "[tag.prefix=v] just adding prefix"
## the commit comment contains "[tag.prefix=v]"

$ gitwerk semver-auto
## increments tag to v1.0.0
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
