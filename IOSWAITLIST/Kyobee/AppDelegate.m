//
//  AppDelegate.m
//  Kyobee
//
//  Created by Jenis Shah on 9/16/16.
//
//

#import "AppDelegate.h"

#import "LoginVC.h"

#import "DetailsVC.h"

#import "GuestListVC.h"

#import "GuestListTwoColumn.h"
#import "GuestListOneColumn.h"
#import "KyobeeGuestList.h"

#import "GuestListFourthColumn.h"

#import <Fabric/Fabric.h>
#import <Crashlytics/Crashlytics.h>

@interface AppDelegate ()

@end

@implementation AppDelegate
@synthesize navController;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    appDelegate =(AppDelegate*)[[UIApplication sharedApplication]delegate];
    
    [Fabric with:@[[Crashlytics class]]];
    
    [self setUP];
    
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    
    if ([UIDevice currentDevice].userInterfaceIdiom == UIUserInterfaceIdiomPad)
    { // is iPad
        
        ;
        
        LoginVC *objLVC = [[LoginVC alloc] initWithNibName:@"LoginVC" bundle:nil];
        
        self.navController = [[UINavigationController alloc] initWithRootViewController:objLVC];
        
        
        /*if([[NSUserDefaults standardUserDefaults] boolForKey:@"rememberMe"] == YES)
        {
            DetailsVC *detailsVC = [[DetailsVC alloc] initWithNibName:@"DetailsVC" bundle:nil];
            [self.navController pushViewController:detailsVC animated:YES];
        }*/
        
        
        if([[NSUserDefaults standardUserDefaults] boolForKey:@"rememberMe"] == YES)
        {
            
            if([[NSUserDefaults standardUserDefaults] boolForKey:@"checkinmodeselected"] == YES)
            {
                DetailsVC *detailsVC = [[DetailsVC alloc] initWithNibName:@"DetailsVC" bundle:nil];
                
                
                [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"fromBack"];
                [[NSUserDefaults standardUserDefaults] synchronize];
                
                [self.navController pushViewController:detailsVC animated:YES];
            }
            if([[NSUserDefaults standardUserDefaults] boolForKey:@"displaymodeselected"] == YES)
            {
                
                if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfColumns"] isEqualToString:@""])
                {
                    if([[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfColumns"] isEqualToString:@"1"])
                    {
                        GuestListOneColumn *guestListOneColumn = [[GuestListOneColumn alloc] initWithNibName:@"GuestListOneColumn" bundle:nil];
                        
                        [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"fromBack"];
                        [[NSUserDefaults standardUserDefaults] synchronize];
                        
                        [self.navController pushViewController:guestListOneColumn animated:YES];
                    }
                    if([[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfColumns"] isEqualToString:@"2"])
                    {
                        GuestListTwoColumn *guestListTwoColumn = [[GuestListTwoColumn alloc] initWithNibName:@"GuestListTwoColumn" bundle:nil];
                        
                        [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"fromBack"];
                        [[NSUserDefaults standardUserDefaults] synchronize];
                        
                        [self.navController pushViewController:guestListTwoColumn animated:YES];
                    }
                    if([[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfColumns"] isEqualToString:@"3"])
                    {
                        KyobeeGuestList *kyobeeGuestList = [[KyobeeGuestList alloc] initWithNibName:@"KyobeeGuestList" bundle:nil];
                        
                        [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"fromBack"];
                        [[NSUserDefaults standardUserDefaults] synchronize];
                        
                        [self.navController pushViewController:kyobeeGuestList animated:YES];
                    }
                    if([[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfColumns"] isEqualToString:@"4"])
                    {
                        GuestListFourthColumn *guestListFourthColumn = [[GuestListFourthColumn alloc] initWithNibName:@"GuestListFourthColumn" bundle:nil];
                        
                        [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"fromBack"];
                        [[NSUserDefaults standardUserDefaults] synchronize];
                        
                        [self.navController pushViewController:guestListFourthColumn animated:YES];
                    }
                    
                }
                else
                {
                    KyobeeGuestList *kyobeeGuestList = [[KyobeeGuestList alloc] initWithNibName:@"KyobeeGuestList" bundle:nil];
                    
                    [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"fromBack"];
                    [[NSUserDefaults standardUserDefaults] synchronize];
                    
                    [self.navController pushViewController:kyobeeGuestList animated:YES];
                }
                
                /*GuestListVC *guestListVC = [[GuestListVC alloc] initWithNibName:@"GuestListVC" bundle:nil];
                [self.navController pushViewController:guestListVC animated:YES];*/
                
                
            }
            
            
        }
    }
    
    [[UIApplication sharedApplication] setStatusBarHidden:YES animated:NO];
    
    self.window.rootViewController = self.navController;
    
    return YES;
}

-(void) setUP
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(reachabilityChanged:) name:@"kReachabilityChangedNotification" object:nil];
    
    reachability = [Reachability reachabilityForInternetConnection];
    
    if([reachability currentReachabilityStatus] == NotReachable)
        self.isInternetReachble = FALSE;
    else
        self.isInternetReachble = TRUE;
    
    [reachability startNotifier];
    
}

- (void)reachabilityChanged:(NSNotification *)note
{
    Reachability* curReach = [note object];
    NSParameterAssert([curReach isKindOfClass:[Reachability class]]);
    
    NetworkStatus netStatus = [curReach currentReachabilityStatus];
    
    if(netStatus == NotReachable)
        self.isInternetReachble = FALSE;
    else
        self.isInternetReachble = TRUE;
    
}

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

#pragma mark Activity Indicator Methods -

- (void)startActivityIndicator:(UIView *)view
{
    if(self.activitityIndicator)
        
        [self.activitityIndicator removeFromSuperview];
    
    [self setActivitityIndicator:nil];
    
    self.activitityIndicator = [[MBProgressHUD alloc] initWithView:view];
    
    [view addSubview:self.activitityIndicator];
    
    self.activitityIndicator.dimBackground = NO;
    
    self.activitityIndicator.labelText = @"Please wait...";
    
    [self.activitityIndicator show:YES];
    
}


- (void)stopActivityIndicator

{
    // Regiser for HUD callbacks so we can remove it from the window at the right time
    
    [self.activitityIndicator removeFromSuperview];
    
    if(self.activitityIndicator)
        
        [self setActivitityIndicator:nil];
    
}

@end
