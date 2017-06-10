//
//  GuestListFourthColumn.h
//  Kyobee
//
//  Created by Mayur Pandya on 16/05/17.
//
//

#import <UIKit/UIKit.h>

#import <OrtcClient.h>

#import "AppDelegate.h"

@interface GuestListFourthColumn : UIViewController<UITableViewDelegate,UITableViewDataSource,UIAlertViewDelegate,OrtcClientDelegate>
{
    
    AppDelegate *appDelegate;
    
    NSMutableArray *guestArray;
    
    NSMutableArray *arrayLess30;
    NSMutableArray *arrayLess60;
    NSMutableArray *arrayLess90;
    
    NSMutableArray *arrayLess120;
    
    IBOutlet UIImageView *imgResto;
    
    IBOutlet UITableView *tblView30;
    IBOutlet UITableView *tblView60;
    IBOutlet UITableView *tblView90;
    
    IBOutlet UITableView *tblView120;
    
    
    
    
    IBOutlet UILabel *lblCopyright;
    
    IBOutlet UIButton *btnLogoutFromLongPress;
    IBOutlet UIButton *btnForOptions;
    
    
    //*** For Seetings View
    
    IBOutlet UILabel *lblColumnOneName;
    IBOutlet UILabel *lblColumnTwoName;
    IBOutlet UILabel *lblColumnThreeName;
    
    IBOutlet UILabel *lblColumnFourName;
    
    IBOutlet UILabel *lblGuestOne;
    IBOutlet UILabel *lblGuestTwo;
    IBOutlet UILabel *lblGuestThree;
    IBOutlet UILabel *lblGuestFour;
    
    
    IBOutlet UIView *viewSettings;
    IBOutlet UIView *viewSubSettings;
    IBOutlet UITextField *txtColumns;
    IBOutlet UIButton *btnColumns;
    IBOutlet UITextField *txtRows;
    IBOutlet UIButton *btnRows;
    IBOutlet UITextField *txtColumnOne;
    IBOutlet UITextField *txtColumnTwo;
    IBOutlet UITextField *txtColumnThree;
    
    IBOutlet UITextField *txtColumnFour;
    
    IBOutlet UITableView *tblViewColumnsAndRows;
    IBOutlet UIButton *btnSettingsOK;
    
    IBOutlet UIButton *btnNotPresent;
    IBOutlet UIButton *btnIncomplete;
    
    IBOutlet UILabel *lblPartyOne;
    IBOutlet UILabel *lblPartyTwo;
    IBOutlet UILabel *lblPartyThree;
    
    IBOutlet UILabel *lblPartyFour;
    
    IBOutlet UIButton *btnShowParty;
    
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

- (IBAction)btnNotPresent_clicked:(id)sender;
- (IBAction)btnIncomplete_clicked:(id)sender;

- (IBAction)btnShowParty_clicked:(id)sender;

@property (strong, nonatomic) IBOutlet NSLayoutConstraint *tblColRowToTop;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *heightTblColRow;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *heightViewSubSettings;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *txtColToTop;


//**

@property (strong, nonatomic) IBOutlet NSLayoutConstraint *colOneGuestLead;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *colTwoGuestLead;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *colThreeGuestLead;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *colFourGuestLead;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *partyOneLead;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *partyTwoLead;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *partyThreeLead;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *partyFourLead;

//**

@end
