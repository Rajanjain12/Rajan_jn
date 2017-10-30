//
//  AppDelegate.h
//  Kyobee
//
//  Created by Jenis Shah on 9/16/16.
//  mayur on 02 june 2017
//

#import <UIKit/UIKit.h>

#import "Reachability.h"
#import "MBProgressHUD.h"

/* Apple Credentials
 meetjenis@gmail.com
 //Nikesh1983
 Kyobee@1234
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


/*
 *  获取当前保存在NSUserDefaults的本地语言
 */
#define currentLanguage [NSString stringWithFormat:@"%@", [[NSUserDefaults standardUserDefaults] objectForKey:@"appLanguage"]]

/*
 *  根据获取语言文件所在路径
 *  文件名类型Type为lproj，即.lproj的文件夹。  zh-Hans.lproj和en.lproj
 *  存在NSUserDefaults的适合，中英文就分别设置为zh-Hans和en，不可改变。
 */
#define LanguagePath    [[NSBundle mainBundle] pathForResource:currentLanguage ofType:@"lproj"]

/*
 *  根据键值获取返回转换结果
 */
//#define Localized(key)  [[NSBundle bundleWithPath:LanguagePath] localizedStringForKey:(key) value:nil table:@"Language"]    //table为语言文件名Language.strings

//等同于上面定义的三个宏
//#define Localized(key)  [[NSBundle bundleWithPath:[[NSBundle mainBundle] pathForResource:[NSString stringWithFormat:@"%@",[[NSUserDefaults standardUserDefaults] objectForKey:@"appLanguage"]] ofType:@"lproj"]] localizedStringForKey:(key) value:nil table:@"Language"]








#define logoUrl @"http://jbossdev-kyobee.rhcloud.com/static/orglogos/" // DEV Envireonment
#define ServiceUrl @"http://jbossdev-kyobee.rhcloud.com/kyobee/" // DEV Envireonment
#define ChannelName @"RSNT_GUEST_DEV_" // DEV Envireonment*/



/*
#define logoUrl @"http://jbossqa-kyobee.rhcloud.com/static/orglogos/" // QA Envireonment
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

