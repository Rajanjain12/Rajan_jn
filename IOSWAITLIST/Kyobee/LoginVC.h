//
//  LoginVC.h
//  Kyobee
//
//  Created by Jenis Shah on 9/16/16.
//
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"

@interface LoginVC : UIViewController<UITextFieldDelegate>
{
    AppDelegate *appDelegate;
    
    IBOutlet UIImageView *Img_lohin_popup;
    IBOutlet UILabel *Lbl_Sign_in;
    IBOutlet UITextField *txt_UserName;
    IBOutlet UITextField *txt_Password;
    
    IBOutlet UIButton *btn_Login;
    IBOutlet UIButton *btn_Fogot_Psw;
    IBOutlet UIButton *btn_Remember;
    IBOutlet UILabel *Lbl_Remember_me;
    
    IBOutlet UITextField *txtEmail;
    IBOutlet UIButton *btnSend;
    
     
    IBOutlet UIImageView *Img_Footer_bg;
    IBOutlet UIImageView *Img_Kyobee_logo;
    
    IBOutlet UIView *viewSelectGuestMode;
    
    
    
    UITextField *txtFieldRef;
}
@property (nonatomic,retain) UINavigationController *navController;

- (IBAction)btn_Login_Clicked:(id)sender;
- (IBAction)btn_Fogot_Psw_Clicked:(id)sender;
- (IBAction)btn_Remember_Clicked:(id)sender;
- (IBAction)btnSend_clicked:(id)sender;

@property (strong, nonatomic) IBOutlet NSLayoutConstraint *lblSigninToTop;

- (IBAction)btnCheckinMode_clicked:(id)sender;
- (IBAction)btnDisplayMode_clicked:(id)sender;

- (IBAction)btnPrivacyPolicy_clicked:(id)sender;
@end
