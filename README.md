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

gitwerk has several default functions.

- `clone`
- `log`
- `tag`
- `contextual-semver`

The definitions of these functions can be printed by `prelude` function.

```bash
$ gitwerk prelude
(defn clone [repository]
  (git/clone repository))

(defn log
 ([]
  (log "."))
 ([repodir]
  (-> (git/repo repodir)
      (git/logs))))

(defn tag
  ([]
   (tag "."))
  ([repodir]
   (-> (git/repo repodir)
       (git/tags))))

(defn repl []
  (println "not implemented yet"))

(defn contextual-semver
  ([]
   (contextual-semver "."))
  ([repodir]
   (let [repo (git/repo repodir)
         message (-> repo
                     (git/latest-log)
                     :full-message)
         tag (or (-> repo
                     (git/tags)
                     (semver/latest-tag))
                 (semver/default-version-str))
         new-tag (semver/contextual-semver message tag)]
     (if (not (= tag new-tag))
       (do
         (git/tag repo new-tag)
         {:status :updated
          :old-version tag
          :new-version new-tag})
       {:status :not-updated}))))
```


### contextual-semver

increments version by git log message contexts.

[semver workflow of this repository](https://github.com/rinx/gitwerk/blob/master/.github/workflows/semver.yml) is an example of this subcommand.

```bash
## when the latest tag is v0.0.1
$ git commit -m "[patch] increment patch version"
## the commit comment contains "[patch]"

$ gitwerk contextual-semver
{:status :updated :old-version v0.0.1 :new-version v0.0.2}
## increments tag to v0.0.2

###...

$ git commit -m "[tag.suffix=-alpha] [minor] increment minor version and add suffix"
## the commit comment contains "[minor]" and "[tag.suffix=-alpha]"

$ gitwerk contextual-semver
{:status :updated :old-version v0.0.2 :new-version v0.1.0-alpha}
## increments tag to v0.1.0-alpha

###...

$ git commit -m "[tag.prefix=] [tag.suffix=] [major] increment major version and remove prefix and suffix"
## the commit comment contains "[major]", "[tag.prefix=]" and "[tag.suffix=]"

$ gitwerk contextual-semver
{:status :updated :old-version v0.1.0-alpha :new-version 1.0.0}
## increments tag to 1.0.0

###...

$ git commit -m "[tag.prefix=v] just adding prefix"
## the commit comment contains "[tag.prefix=v]"

$ gitwerk contextual-semver
{:status :updated :old-version 1.0.0 :new-version v1.0.0}
## increments tag to v1.0.0
```


### Define your own functions

You can define your own functions and load them by using `--file` option or `--stdin` option.

```bash
gitwerk --stdin myfunc
(defn myfunc []
  (print "this is my first func"))
```

Available functions are non-side-effecting functions enabled in [sci](https://github.com/borkdude/sci) and the exported functions in gitwerk.internal and gitwerk.prelude namespaces.


## License

Copyright © 2019-2020 rinx

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
