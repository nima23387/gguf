package com.nima.aisuite.device;
public class DeviceProfile {
 public String maker,brand,model,device,release,abi; public int api,cores; public long totalMb,availableMb,storageMb; public boolean root;
 public String toString(){return "Manufacturer: "+maker+"\nBrand: "+brand+"\nModel: "+model+"\nDevice: "+device+"\nAndroid: "+release+" (API "+api+")\nABI: "+abi+"\nCPU cores: "+cores+"\nRAM total: "+totalMb+" MB\nRAM available: "+availableMb+" MB\nFree storage: "+storageMb+" MB\nRoot detected: "+root;}
}
