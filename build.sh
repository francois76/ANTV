#!/bin/bash

# ANTV Build Script
# Facilite le build du projet avec les bonnes configurations

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configure JAVA_HOME
if [ -d "/Applications/Developpement/Android Studio.app/Contents/jbr/Contents/Home" ]; then
    export JAVA_HOME="/Applications/Developpement/Android Studio.app/Contents/jbr/Contents/Home"
    echo -e "${GREEN}✓ Using Android Studio JDK${NC}"
elif [ -n "$JAVA_HOME" ]; then
    echo -e "${GREEN}✓ Using existing JAVA_HOME: $JAVA_HOME${NC}"
else
    echo -e "${RED}✗ JAVA_HOME not set and Android Studio JDK not found${NC}"
    echo "Please install Android Studio or set JAVA_HOME manually"
    exit 1
fi

# Check Java version
JAVA_VERSION=$("$JAVA_HOME/bin/java" -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -ge 17 ]; then
    echo -e "${GREEN}✓ Java version OK: $JAVA_VERSION${NC}"
else
    echo -e "${YELLOW}⚠ Java version $JAVA_VERSION detected, Java 17+ recommended${NC}"
fi

# Function to show usage
show_usage() {
    echo "Usage: $0 [command]"
    echo ""
    echo "Commands:"
    echo "  android-debug    Build Android debug APK"
    echo "  android-release  Build Android release APK"
    echo "  android-run      Install and run Android app on connected device"
    echo "  desktop          Run desktop application"
    echo "  clean            Clean all build artifacts"
    echo "  test             Run all tests"
    echo "  help             Show this help message"
    echo ""
}

# Parse command
case "$1" in
    android-debug)
        echo -e "${GREEN}Building Android debug APK...${NC}"
        ./gradlew :androidApp:assembleDebug
        echo -e "${GREEN}✓ APK built successfully!${NC}"
        echo "Location: androidApp/build/outputs/apk/debug/androidApp-debug.apk"
        ;;

    android-release)
        echo -e "${GREEN}Building Android release APK...${NC}"
        ./gradlew :androidApp:assembleRelease
        echo -e "${GREEN}✓ APK built successfully!${NC}"
        echo "Location: androidApp/build/outputs/apk/release/androidApp-release.apk"
        ;;

    android-run)
        echo -e "${GREEN}Installing and running Android app...${NC}"
        ./gradlew :androidApp:installDebug
        adb shell am start -n fr.fgognet.antv.debug/fr.fgognet.antv.activity.main.MainActivity
        echo -e "${GREEN}✓ App launched!${NC}"
        ;;

    desktop)
        echo -e "${GREEN}Running desktop application...${NC}"
        ./gradlew desktopApp:run
        ;;

    clean)
        echo -e "${GREEN}Cleaning build artifacts...${NC}"
        ./gradlew clean
        echo -e "${GREEN}✓ Clean complete!${NC}"
        ;;

    test)
        echo -e "${GREEN}Running tests...${NC}"
        ./gradlew test
        ;;

    help|"")
        show_usage
        ;;

    *)
        echo -e "${RED}Unknown command: $1${NC}"
        show_usage
        exit 1
        ;;
esac
