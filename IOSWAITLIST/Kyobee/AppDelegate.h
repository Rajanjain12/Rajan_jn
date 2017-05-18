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


#define logoUrl @"http://jbossdev-kyobee.rhcloud.com/static/orglogos/" // DEV Envireonment
#define ServiceUrl @"http://jbossdev-kyobee.rhcloud.com/kyobee/" // DEV Envireonment
#define ChannelName @"RSNT_GUEST_DEV_" // DEV Envireonment*/


/*#define logoUrl @"http://jbossqa-kyobee.rhcloud.com/static/orglogos/" // QA Envireonment
#define ServiceUrl @"http://jbossqa-kyobee.rhcloud.com/kyobee/" // QA Envireonment
#define ChannelName @"RSNT_GUEST_QA_" // QA Envireonment*/



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

