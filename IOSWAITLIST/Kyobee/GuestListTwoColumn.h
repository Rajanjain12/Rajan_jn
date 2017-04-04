//
//  GuestListTwoColumn.h
//  Kyobee
//
//  Created by Mayur Pandya on 03/04/17.
//
//

#import <UIKit/UIKit.h>

#import <OrtcClient.h>

#import "AppDelegate.h"

@interface GuestListTwoColumn : UIViewController<UITableViewDelegate,UITableViewDataSource,UIAlertViewDelegate,OrtcClientDelegate>
{
    
    AppDelegate *appDelegate;
    
    NSMutableArray *guestArray;
    
    NSMutableArray *arrayLess30;
    NSMutableArray *arrayLess60;
    
    IBOutlet UIImageView *imgResto;
    
    IBOutlet UITableView *tblView30;
    IBOutlet UITableView *tblView60;
    
    IBOutlet UILabel *lblNoUser30;
    IBOutlet UILabel *lblNoUser60;
    
    
    IBOutlet UILabel *lblCopyright;
    
    IBOutlet UIButton *btnLogoutFromLongPress;
    IBOutlet UIButton *btnForOptions;
    
    //*** For Seetings View
    
    IBOutlet UILabel *lblColumnOneName;
    IBOutlet UILabel *lblColumnTwoName;
    
    
    IBOutlet UIView *viewSettings;
    IBOutlet UIView *viewSubSettings;
    IBOutlet UITextField *txtColumns;
    IBOutlet UIButton *btnColumns;
    IBOutlet UITextField *txtRows;
    IBOutlet UIButton *btnRows;
    IBOutlet UITextField *txtColumnOne;
    IBOutlet UITextField *txtColumnTwo;
    IBOutlet UITextField *txtColumnThree;
    IBOutlet UITableView *tblViewColumnsAndRows;
    IBOutlet UIButton *btnSettingsOK;
    
    
    NSMutableArray *columnsArray;
    NSMutableArray *rowsArray;
    
    UITextField *txtFieldRef;
}

- (IBAction)btnRefresh_clicked:(id)sender;

@property OrtcClient* ortcClient;

//*** For Seetings View

- (IBAction)btnCloseSettings_clicked:(id)sender;
- (IBAction)btnColumns_clicked:(id)sender;
- (IBAction)btnRows_clicked:(id)sender;
- (IBAction)btnSettingsOK_clicked:(id)sender;

@property (strong, nonatomic) IBOutlet NSLayoutConstraint *tblColRowToTop;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *heightTblColRow;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *heightViewSubSettings;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *txtColToTop;

@end
