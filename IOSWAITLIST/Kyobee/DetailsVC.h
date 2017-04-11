//
//  DetailsVC.h
//  Kyobee
//
//  Created by Jenis Shah on 9/16/16.
//
//

#import <UIKit/UIKit.h>

#import <OrtcClient.h>

#import "AppDelegate.h"

#import "WBTableView.h"

#import "PTEHorizontalTableView.h"

@interface DetailsVC : UIViewController<OrtcClientDelegate,UIAlertViewDelegate,WBTableViewDataSource,WBTableViewDelegate,UITableViewDelegate,UITableViewDataSource,PTETableViewDelegate>
{
    AppDelegate *appDelegate;
    
    IBOutlet UIView *First_View;
    
    IBOutlet UIButton *btnLogout;// Mayur
    IBOutlet UIButton *btnTopLeft;
    IBOutlet UIButton *btnBottomLeft;
    IBOutlet UIButton *btnTopRight;
    
    //WBTableView * _tableViewWB;
    IBOutlet WBTableView *_tableViewWB;
    
    IBOutlet UIImageView *Img_User_Logo;
    IBOutlet UILabel *lbl_Count;
    IBOutlet UILabel *Lbl_waiting;
    IBOutlet UIImageView *Img_Standing_Line;
    IBOutlet UILabel *Lbl_Now_serving;
    IBOutlet UILabel *Lbl_Now_serving_Count;
    IBOutlet UILabel *Lbl_Est_Wait_Time;
    IBOutlet UILabel *Lbl_Est_Time;
    IBOutlet UIButton *Btn_Press_Here_Checkin;
    IBOutlet UIImageView *Img_Footer_Bg;
    IBOutlet UIImageView *Img_Kyobee_Logo;
    IBOutlet UIButton *btnInternetConnection;
    IBOutlet UILabel *lblClientBase;
    
    NSTimer *timerInternet;
    NSTimer *SpeedConnection;
    
    IBOutlet UILabel *lblHours;
    IBOutlet UILabel *lblColon;
    IBOutlet UILabel *lblMinutes;
    
    IBOutlet UILabel *lblWelcome;
    IBOutlet UILabel *lblFooter;
    
    
    IBOutlet UIButton *btnForOptions;
    
    
    
    IBOutlet UIView *form_View;
    IBOutlet UIImageView *Img_Bg_Blure;
    IBOutlet UIImageView *Img_Required_Bg;
    IBOutlet UILabel *Lbl_Required;
    IBOutlet UILabel *lblAdd_New_Guest_bg;
    IBOutlet UIButton *Btn_Close;
    
    IBOutlet UITextField *txt_Name;
    IBOutlet UIButton *btn_Cell_Phone;
    IBOutlet UIButton *btn_Email;
    IBOutlet UITextField *txt_Phone_Or_Mail;
    IBOutlet UITextField *txt_your_party;
    IBOutlet UIImageView *Img_seating_Bg;
    IBOutlet UILabel *Lbl_Seating_Preference;
    IBOutlet UIButton *Btn_Patio;
    IBOutlet UILabel *Lbl_Patio;
    IBOutlet UIButton *Btn_Bar;
    IBOutlet UILabel *Lbl_Bar;
    IBOutlet UIButton *Btn_First_Available;
    IBOutlet UILabel *Lbl_First_Available;
    IBOutlet UIButton *Btn_Booth;
    IBOutlet UILabel *Lbl_Booth;
    IBOutlet UIButton *Btn_Table;
    IBOutlet UILabel *Lbl_Table;
    IBOutlet UIButton *Btn_Promotions_Specials;
    IBOutlet UILabel *Lbl_Promotions_Specials;
    IBOutlet UIButton *Btn_Add_me_wait_list;
    UITableView *tblViewSeatPref;// Mayur
    NSMutableArray *seatPrefArray;
    NSMutableArray *selectedPref;
    IBOutlet UITableView *tblViewParties;
    
    IBOutlet UILabel *lblOnlyUSNumberValid;
    IBOutlet UILabel *lblPoorInternet;
    NSTimer *checkinTimerInternet;
    NSTimer *hidePoorInternet;
    
    
    IBOutlet UIView *Thank_View;
    IBOutlet UIImageView *Img_thank_you_Popup;
    IBOutlet UILabel *Lbl_One;
    IBOutlet UILabel *Lbl_Number;
    IBOutlet UILabel *Lbl_Two;
    IBOutlet UILabel *Lbl_Three;
    IBOutlet UIButton *Btn_Thank_you_ok;
    
    
    UITextField *txtFieldRef;
}

@property OrtcClient* ortcClient;

- (IBAction)btnLogout_clicked:(id)sender; // Mayur
- (IBAction)btnInternetConnection_clicked:(id)sender;


- (IBAction)btn_Phone_Or_Mail_Clicked:(id)sender;
- (IBAction)Btn_Close_Clicked:(id)sender;
- (IBAction)Btn_Press_Here_Checkin_Clicked:(id)sender;

- (IBAction)Btn_Patio_Clicked:(id)sender;
- (IBAction)Btn_Bar_Clicked:(id)sender;
- (IBAction)Btn_First_Available_Clicked:(id)sender;
- (IBAction)Btn_Booth_Clicked:(id)sender;
- (IBAction)Btn_Table_Clicked:(id)sender;
- (IBAction)btnParties_clicked:(id)sender;

- (IBAction)Btn_Promotions_Specials_Clicked:(id)sender;
- (IBAction)Btn_Add__me_wait_list_Clicked:(id)sender;
- (IBAction)Btn_Thank_you_ok_Clicked:(id)sender;

//void UIAccessibilityRequestGuidedAccessSession(BOOL enable, void(^completionHandler)(BOOL didSucceed));


@property (strong, nonatomic) IBOutlet NSLayoutConstraint *txtNameToTop;

// For Home View

@property (strong, nonatomic) IBOutlet NSLayoutConstraint *nowServingToValue;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *estTimeToValue;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *btnCheckinToBottom;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *partiesWaitingToVerticalLine;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *nowServingTopAlign;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *estTimeTopAlign;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *btnCheckinToTop;

//

// For Form View

@property (strong, nonatomic) IBOutlet NSLayoutConstraint *formViewWidth;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *formViewCenter;

@property (strong, nonatomic) IBOutlet NSLayoutConstraint *txtPhoneFieldCenter;

//

// For Thank You View

@property (strong, nonatomic) IBOutlet NSLayoutConstraint *thankYouViewWidth;


//

@end
