# kith-mobile

android side of kith. compose ui, foreground sync service, ble proximity wake.

build the rust core first: `cd ../kith-linux && cargo ndk -t arm64-v8a -t armeabi-v7a -t x86_64 -o ../kith-mobile/app/src/main/jniLibs build --release -p kith-core`

then `./gradlew assembleDebug` or open in android studio.
