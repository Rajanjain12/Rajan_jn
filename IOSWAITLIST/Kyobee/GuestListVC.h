//
//  GuestListVC.h
//  Kyobee
//
//  Created by Mayur Pandya on 11/02/17.
//
//

#import <UIKit/UIKit.h>

#import <OrtcClient.h>

#import "AppDelegate.h"

@interface GuestListVC : UIViewController<UIAlertViewDelegate,OrtcClientDelegate,UITableViewDelegate,UITableViewDataSource>
{
    AppDelegate *appDelegate;
    
    NSMutableArray *guestArray;
    
    IBOutlet UIImageView *imgResto;
    IBOutlet UITableView *tblViewGuestList;
}

@property OrtcClient* ortcClient;

- (IBAction)btnLogout_clicked:(id)sender;
- (IBAction)btnRefresh_clicked:(id)sender;

@end
