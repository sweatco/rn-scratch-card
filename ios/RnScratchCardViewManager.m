#import <React/RCTViewManager.h>

@interface RCT_EXTERN_MODULE(RnScratchCardViewManager, RCTViewManager)

RCT_EXPORT_VIEW_PROPERTY(image, NSDictionary)
RCT_EXPORT_VIEW_PROPERTY(brushWidth, NSNumber)
RCT_EXPORT_VIEW_PROPERTY(onScratch, RCTDirectEventBlock)

+ (BOOL)requiresMainQueueSetup
{
  return YES;  // module initialization relies on calling UIKit
}

@end
