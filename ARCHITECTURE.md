# Architecture
- app: legacy-compatible UI
- device: live device capability detection
- memory: RAM diagnostics
- advisor: conservative model resource assessment
- gguf: GGUF header validation
- compiler: NimaScript compiler and bytecode VM
- engines: future native inference adapters

A model may only be called runnable after a matching native engine is present, its ABI/API requirements pass, and an engine self-test succeeds.
