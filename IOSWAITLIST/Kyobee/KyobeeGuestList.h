//
//  KyobeeGuestList.h
//  Kyobee
//
//  Created by Mayur Pandya on 28/03/17.
//
//

#import <UIKit/UIKit.h>

#import <OrtcClient.h>

#import "AppDelegate.h"

@interface KyobeeGuestList : UIViewController<UITableViewDelegate,UITableViewDataSource,UIAlertViewDelegate,OrtcClientDelegate>
{
    
    AppDelegate *appDelegate;
    
    NSMutableArray *guestArray;
    
    IBOutlet UIImageView *imgResto;
    
    IBOutlet UITableView *tblView30;
    IBOutlet UITableView *tblView60;
    IBOutlet UITableView *tblView90;
    
    IBOutlet UILabel *lblCopyright;
    
    IBOutlet UIButton *btnLogoutFromLongPress;
}

- (IBAction)btnRefresh_clicked:(id)sender;

@property OrtcClient* ortcClient;

@end
