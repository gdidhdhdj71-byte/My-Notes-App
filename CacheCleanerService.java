package com.example.zerocleaner;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.List;

public class CacheCleanerService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getSource() == null) return;

        AccessibilityNodeInfo nodeInfo = event.getSource();

        // ୧. "Storage" ବା "ସଂରକ୍ଷଣ" ଲେଖାକୁ ଖୋଜି ତାହା ଉପରେ କ୍ଲିକ୍ କରିବ
        List<AccessibilityNodeInfo> storageNodes = nodeInfo.findAccessibilityNodeInfosByText("Storage");
        if (storageNodes != null && !storageNodes.isEmpty()) {
            for (AccessibilityNodeInfo node : storageNodes) {
                if (node.isClickable()) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return;
                } else if (node.getParent() != null && node.getParent().isClickable()) {
                    node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return;
                }
            }
        }

        // ୨. Storage ଭିତରକୁ ଗଲା ପରେ "Clear cache" ବା "କ୍ୟାଶ୍ ସଫା କରନ୍ତୁ" ବଟନ୍ ଖୋଜି କ୍ଲିକ୍ କରିବ
        List<AccessibilityNodeInfo> cacheNodes = nodeInfo.findAccessibilityNodeInfosByText("Clear cache");
        if (cacheNodes != null && !cacheNodes.isEmpty()) {
            for (AccessibilityNodeInfo node : cacheNodes) {
                if (node.isEnabled()) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    
                    // କ୍ୟାଶ୍ କ୍ଲିନ୍ ହେବା ପରେ ଅଟୋମେଟିକ୍ ବ୍ୟାକ୍ (Back) ହୋଇଯିବ
                    performGlobalAction(GLOBAL_ACTION_BACK);
                }
            }
        }
    }

    @Override
    public void onInterrupt() {
        // ସର୍ଭିସ୍ ବନ୍ଦ ହେଲେ ଏହା କାମ କରିବ
    }
}
