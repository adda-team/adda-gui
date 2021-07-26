#!/bin/sh
# Copyright 2000-2021 ADDA GUI

# ---------------------------------------------------------------------
# ADDA GUI startup script.
# ---------------------------------------------------------------------


message()
{
  TITLE="Cannot start ADDA GUI"
  if [ -n "$(command -v zenity)" ]; then
    zenity --error --title="$TITLE" --text="$1" --no-wrap
  elif [ -n "$(command -v kdialog)" ]; then
    kdialog --error "$1" --title "$TITLE"
  elif [ -n "$(command -v notify-send)" ]; then
    notify-send "ERROR: $TITLE" "$1"
  elif [ -n "$(command -v xmessage)" ]; then
    xmessage -center "ERROR: $TITLE: $1"
  else
    printf "ERROR: %s\n%s\n" "$TITLE" "$1"
  fi
}

if [ -z "$(command -v uname)" ] || [ -z "$(command -v realpath)" ] || [ -z "$(command -v dirname)" ] || [ -z "$(command -v cat)" ] || \
   [ -z "$(command -v egrep)" ]; then
  TOOLS_MSG="Required tools are missing:"
  for tool in uname realpath egrep dirname cat ; do
     test -z "$(command -v $tool)" && TOOLS_MSG="$TOOLS_MSG $tool"
  done
  message "$TOOLS_MSG (SHELL=$SHELL PATH=$PATH)"
  exit 1
fi

# shellcheck disable=SC2034
GREP_OPTIONS=''
OS_TYPE=$(uname -s)
OS_ARCH=$(uname -m)

# ---------------------------------------------------------------------
# Ensure $IDE_HOME points to the directory where the IDE is installed.
# ---------------------------------------------------------------------
IDE_BIN_HOME=$(dirname "$(realpath "$0")")
#IDE_HOME=$(dirname "${IDE_BIN_HOME}")
IDE_HOME="$IDE_BIN_HOME"
PRODUCT_VENDOR="adda-gui"
JAR_PATH="$IDE_HOME/lib/$PRODUCT_VENDOR.jar"


# ---------------------------------------------------------------------
# Locate a JRE installation directory command -v will be used to run the IDE.
# Try (in order): $IDEA_JDK, .../jbr[-x86], $JDK_HOME, $JAVA_HOME, "java" in $PATH.
# ---------------------------------------------------------------------
# shellcheck disable=SC2154

BITS=""

if [ -z "$JRE" ] && [ "$OS_TYPE" = "Linux" ]; then
  if [ "$OS_ARCH" = "x86_64" ] && [ -d "$IDE_HOME/jbr" ]; then
    JRE="$IDE_HOME/jbr"
  elif [ -d "$IDE_HOME/jbr-x86" ] && "$IDE_HOME/jbr-x86/bin/java" -version > /dev/null 2>&1 ; then
    JRE="$IDE_HOME/jbr-x86"
  fi
fi

# shellcheck disable=SC2153
if [ -z "$JRE" ]; then
  if [ -n "$JDK_HOME" ] && [ -x "$JDK_HOME/bin/java" ]; then
    JRE="$JDK_HOME"
  elif [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then
    JRE="$JAVA_HOME"
  fi
fi

if [ -z "$JRE" ]; then
  JAVA_BIN=$(command -v java)
else
  JAVA_BIN="$JRE/bin/java"
fi

if [ -z "$JAVA_BIN" ]; then
  X86_JRE_URL="https://download.jetbrains.com/idea/jbr-for-211.7628.21-linux-x86.tar.gz"
  # shellcheck disable=SC2166
  if [ -n "$X86_JRE_URL" ] && [ ! -d "$IDE_HOME/jbr-x86" ] && [ "$OS_ARCH" = "i386" -o "$OS_ARCH" = "i686" ]; then
    message "To run ADDA GUI on a 32-bit system, please download 32-bit Java runtime from \"$X86_JRE_URL\" and unpack it into \"jbr-x86\" directory."
  else
    message "No JRE found. Please make sure \$IDEA_JDK, \$JDK_HOME, or \$JAVA_HOME point to valid JRE installation."
  fi
  exit 1
fi

if [ ! -x "$JAVA_BIN" ]; then
  # shellcheck disable=SC2166
  echo "please set java as executable file:"
  echo "sudo chmod +x $JAVA_BIN"
  message "\"$JAVA_BIN\" is not executable"
  exit 1
fi

if [ -n "$JRE" ] && [ -r "$JRE/release" ]; then
  egrep -q -E -e "OS_ARCH=\"(x86_64|amd64)\"" "$JRE/release" && BITS="64" || BITS=""
else
  test "${OS_ARCH}" = "x86_64" && BITS="64" || BITS=""
fi

# ---------------------------------------------------------------------
# Run the ADDA GUI
# ---------------------------------------------------------------------


IFS="$(printf '\n\t')"
# shellcheck disable=SC2086
"$JAVA_BIN" \
  -cp "$JAR_PATH" adda.Main\
  "$@"

