//
//  AppDelegate.h
//  Kyobee
//
//  Created by Jenis Shah on 9/16/16.
//
//

#import <UIKit/UIKit.h>

#import "Reachability.h"
#import "MBProgressHUD.h"

/* Apple Credentials
 meetjenis@gmail.com
 Nikesh1983
*/

/* Real Time Credentials
 info@kyobee.com
 Acreation1
*/

/* Kyobee Web Portal
 jbossdev-kyobee.rhcloud.com/Rsnt/views/home.seam
 ID : jkim@kyobee.com
 PWD : jaekim
*/

@interface AppDelegate : UIResponder <UIApplicationDelegate>
{
    
    AppDelegate *appDelegate;
    
    Reachability *reachability;
    
}

@property(nonatomic,strong) MBProgressHUD* activitityIndicator;

@property (strong, nonatomic) UIWindow *window;

@property (nonatomic,retain) UINavigationController *navController;


//*****

- (void)startActivityIndicator:(UIView *)view;
- (void)stopActivityIndicator;
@property (assign) BOOL isInternetReachble;
//****

@end

