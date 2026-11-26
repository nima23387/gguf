package com.nima.aisuite.advisor;
import com.nima.aisuite.device.DeviceProfile;
public final class ModelAdvisor {
 public static Assessment assess(DeviceProfile p,long bytes){long mb=bytes<0?0:bytes/1048576L;long need=(long)(mb*1.3)+256;
 if(p.api<21)return new Assessment("LEGACY / RESTRICTED","Android API "+p.api+" is too old for most current native AI runtimes.");
 if(p.availableMb<need)return new Assessment("NOT RECOMMENDED","Estimated memory requirement "+need+" MB; available "+p.availableMb+" MB.");
 if(p.availableMb<need+512)return new Assessment("BORDERLINE","Low memory headroom. Runtime and context size can increase usage.");
 return new Assessment("POTENTIALLY COMPATIBLE","Memory estimate passes. ABI, model architecture and runtime self-test still need verification.");}
 public static String[] rows(DeviceProfile p){return new String[]{"DEVICE: "+p.model+" / "+p.abi,"TEXT: small quantized GGUF models are the most practical category.","CODING: compact code models need additional RAM and supported tokenizer/runtime.","IMAGE: requires a separate image-generation engine.","AUDIO: requires dedicated speech/audio models and runtime.","VIDEO: requires specialized engines and usually substantial compute.","Model profiles are guidance only; no model is marked runnable without a working engine self-test."};}
 public static class Assessment{public String state,reason;Assessment(String s,String r){state=s;reason=r;}}
}
