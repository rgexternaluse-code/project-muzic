#!/bin/sh

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls -ld "$PRG"
    link=`expr "$PRG" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

warn () {
    echo "$*"
}

die () {
    echo
    echo "$*"
    echo
    exit 1
}

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
darwin=false
nonstop=false
case "`uname`" in
  CYGWIN* )
    cygwin=true
    ;;
  Darwin* )
    darwin=true
    ;;
  MSYS* | MINGW* )
    msys=true
    ;;
  NONSTOP* )
    nonstop=true
    ;;
esac

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

# Increase the maximum file descriptors if we can.
if [ "$cygwin" = "false" -a "$darwin" = "false" -a "$nonstop" = "false" ] ; then
    MAX_FD_LIMIT=`ulimit -H -n`
    if [ $? -eq 0 ] ; then
        if [ "$MAX_FD" = "maximum" -o "$MAX_FD" = "max" ] ; then
            MAX_FD="$MAX_FD_LIMIT"
        fi
        ulimit -n $MAX_FD
        if [ $? -ne 0 ] ; then
            warn "Could not set maximum file descriptor limit: $MAX_FD"
        fi
    else
        warn "Could not query maximum file descriptor limit: $MAX_FD_LIMIT"
    fi
fi

# For Darwin, add options to specify how the application appears in the dock
if $darwin; then
    GRADLE_OPTS="$GRADLE_OPTS \"-Xdock:name=$APP_NAME\" \"-Xdock:icon=$APP_HOME/media/gradle.icns\""
fi

# For Cygwin or MSYS, switch paths to Windows format before running java
if [ "$cygwin" = "true" -o "$msys" = "true" ] ; then
    APP_HOME=`cygpath --path --mixed "$APP_HOME"`
    CLASSPATH=`cygpath --path --mixed "$CLASSPATH"`

    JAVACMD=`cygpath --unix "$JAVACMD"`

    # We build the pattern for arguments to be converted via cygpath
    ROOTDIRSRAW=`find -L / -maxdepth 3 -type d -name java >/dev/null 2>&1 && find -L / -maxdepth 3 -type d -name java | head -1`
    # Add a user-defined pattern to the cygpath arguments
    if [ "$GRADLE_CYGWIN_HOME" != "" ] ; then
        CLASSPATH=`cygpath --path --mixed "$GRADLE_CYGWIN_HOME/lib/gradle-wrapper.jar:$CLASSPATH"`
    fi
    # wrapper values
    for jarfile in "$APP_HOME"/gradle/wrapper/*.jar
    do
        if [ -f "$jarfile" ] ; then
            case $jarfile in
                *gradle-wrapper.jar )
                    CLASSPATH=`cygpath --path --mixed "$jarfile:$CLASSPATH"`
                    break
                    ;;
            esac
        fi
    done
    # Determine the Java command to use to start the JVM (version check)
    COMMAND=`cygpath --unix "$JAVACMD"`
fi

# Grab all arguments and save them
APP_ARGS="$*"

# Regular expression matching a number
NUM_RE="^-?[0-9]\+$"

# Separate any jar files in the argument list from the flags so that we can do an intelligent default.
for arg in "$@" ; do
    if [[ "$arg" =~ $NUM_RE ]] ; then
        # shellcheck disable=SC2206
        NUMBER_ARGS+=($arg)
    else
        case $arg in                                   #(
          /*)   # absolute file path is passed
                # shellcheck disable=SC2206
                GRADLE_OPTS+=("$arg")
                ;;          #(
          ?:/*) # partial file path is passed
                # shellcheck disable=SC2206
                GRADLE_OPTS+=("$arg")
                ;;          #(
          *)    # anything else
                # In principle, we could write this as:
                # [[ "$arg" =~ ^--.* ]] && GA_JVM_OPTS+=( "$arg" ) || GA_INIT_DIR+=( "$arg" )
                # but now we actually have three special cases, so let me write it a bit more readably
                case "$arg" in                          #(
                  --debug)   set -x   ;;                #(
                  --info )   set -v   ;;                #(
                  --stop )   exit 0   ;;                #(
                  *)         GRADLE_OPTS+=("$arg") ;;
                esac
                ;;
        esac
    fi
done

exec "$JAVACMD" "${GRADLE_OPTS[@]}" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
