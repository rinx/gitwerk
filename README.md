# gitwerk

[![LICENSE](https://img.shields.io/github/license/rinx/gitwerk)](https://github.com/rinx/gitwerk/blob/master/LICENSE)
[![release](https://img.shields.io/github/v/release/rinx/gitwerk)](https://github.com/rinx/gitwerk/releases)
[![Docker Pulls](https://img.shields.io/docker/pulls/rinx/gitwerk.svg?style=flat-square)](https://hub.docker.com/r/rinx/gitwerk)
[![GitHub Actions: Run gitwerk](https://github.com/rinx/gitwerk/workflows/Run%20gitwerk/badge.svg)](https://github.com/rinx/gitwerk/actions)
[![GitHub Actions: Build Docker image](https://github.com/rinx/gitwerk/workflows/Build%20docker%20image/badge.svg)](https://github.com/rinx/gitwerk/actions)
[![GitHub Actions: Build Native image](https://github.com/rinx/gitwerk/workflows/Build%20native%20image/badge.svg)](https://github.com/rinx/gitwerk/actions)

gitwerk is a CLI tool for supporting Git(Hub) operations on CI.

## Install

It is available to download native binaries for Linux and macOS from the [latest release](https://github.com/rinx/gitwerk/releases/latest).

## Usage

gitwerk has several subcommands.

### semver-auto

increments version by git log message contexts.

[semver workflow of this repository](https://github.com/rinx/gitwerk/blob/master/.github/workflows/semver.yml) is an example of this subcommand.

```bash
## when the latest tag is v0.0.1
$ git commit -m "[patch] increment patch version"
## the commit comment contains "[patch]"

$ gitwerk semver-auto
{:status :updated :old-version v0.0.1 :new-version v0.0.2}
## increments tag to v0.0.2

###...

$ git commit -m "[tag.suffix=-alpha] [minor] increment minor version and add suffix"
## the commit comment contains "[minor]" and "[tag.suffix=-alpha]"

$ gitwerk semver-auto
{:status :updated :old-version v0.0.2 :new-version v0.1.0-alpha}
## increments tag to v0.1.0-alpha

###...

$ git commit -m "[tag.prefix=] [tag.suffix=] [major] increment major version and remove prefix and suffix"
## the commit comment contains "[major]", "[tag.prefix=]" and "[tag.suffix=]"

$ gitwerk semver-auto
{:status :updated :old-version v0.1.0-alpha :new-version 1.0.0}
## increments tag to 1.0.0

###...

$ git commit -m "[tag.prefix=v] just adding prefix"
## the commit comment contains "[tag.prefix=v]"

$ gitwerk semver-auto
{:status :updated :old-version 1.0.0 :new-version v1.0.0}
## increments tag to v1.0.0
```

### semver

prints incremented version.

```bash
## when the latest tag is v0.0.1

$ gitwerk semver patch
v0.0.2

$ gitwerk semver minor
v0.1.0

$ gitwerk semver major
v1.0.0
```

### sci

gitwerk supports to run user-defined scripts written in clojure (powered by [borkdude/sci](https://github.com/borkdude/sci)).

```bash
## can read stdin
$ echo '(semver ctx ["patch"])' | gitwerk sci
v0.0.2

## also read a file as a script
$ cat examples/example1.clj
(semver ctx ["patch"])
$ gitwerk sci examples/example1.clj
v0.0.3

## fetch executed command result and modify returned message
$ cat examples/example2.clj
(let [res (semver-auto ctx nil)
      status (get-in res [:console-out :status])
      oldv (get-in res [:console-out :old-version])
      newv (get-in res [:console-out :new-version])]
  (if (= status :updated)
    (str "Version updated: " oldv " -> " newv)
    "Version not updated"))
$ gitwerk sci examples/example2.clj
Version not updated

$ git commit --allow-empty -m "[patch] version updated"
$ gitwerk sci examples/example2.clj
Version updated: v0.0.3 -> v0.0.4

## http get/post using clj-http-lite.client
$ echo '(-> (http/get "https://api.github.com/repos/rinx/gitwerk/issues") pprint)' | gitwerk sci

## parse json and convert to clj map
$ echo '(-> (http/get "https://api.github.com/repos/rinx/gitwerk/issues") :body (json/read-value) pprint)' | gitwerk sci
```

## License

Copyright © 2019 rinx

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
