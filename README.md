# Nima AI Suite 1.0.0 — Full Source

Offline-first Android application source. `minSdkVersion 7` (Android 2.1), Java 7 source compatibility, Gradle 5.6.4 / Android Gradle Plugin 3.5.4.

## Implemented in this source
- Device analyzer: manufacturer, model, device, Android API, ABI, CPU core count, RAM and free storage.
- RAM diagnostics: Android memory availability and `/proc/meminfo` values.
- Offline model advisor: conservative memory/API guidance.
- GGUF header inspector: magic, version, tensor count and metadata count.
- NimaScript compiler and virtual machine: integer expressions, variables and print statements.
- GitHub Actions workflow: unit tests, debug APK build and artifact upload.

## NimaScript
```
let width = 7;
let height = 5;
print width * height;
```

## Honest implementation boundary
This source does not bundle llama.cpp or another native LLM inference engine. It does not claim that GGUF header inspection is model inference. It also does not implement GGUF tensor quantization, image generation, audio generation or video generation. Those require separate native runtimes and model-specific pipelines.

The app can target API 7 at the Java layer, but modern native AI engines have higher Android/API/ABI requirements. Current llama.cpp Android build documentation, for example, describes a modern NDK path and arm64-v8a/API 28 configuration: https://github.com/ggml-org/llama.cpp/blob/master/docs/android.md

## Build
Push this project to GitHub. The workflow builds `app-debug.apk` and uploads it as a workflow artifact.
