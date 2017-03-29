//
//  LoginVC.m
//  Kyobee
//
//  Created by Jenis Shah on 9/16/16.
//
//

#import "LoginVC.h"
#import "DetailsVC.h"

#import "KyobeeGuestList.h"

#import "GuestListVC.h"

@interface MyTextFieldLogin : UITextField

@property (nonatomic) IBInspectable CGFloat padding;

@end

@implementation MyTextFieldLogin

@synthesize padding;

- (CGRect)textRectForBounds:(CGRect)bounds
{
    return CGRectMake(bounds.origin.x + 15, bounds.origin.y, bounds.size.width-15, bounds.size.height);
}

- (CGRect)editingRectForBounds:(CGRect)bounds
{
    return CGRectMake(bounds.origin.x + 15, bounds.origin.y, bounds.size.width-15, bounds.size.height);
}
@end

@interface LoginVC ()

@end

@implementation LoginVC

- (void)viewDidLoad
{
    appDelegate =(AppDelegate*)[[UIApplication sharedApplication]delegate];
    
    self.navigationController.navigationBarHidden = TRUE;
    
    UIView *paddingView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, 0)];
    txt_UserName.leftView = paddingView;
    txt_UserName.leftViewMode = UITextFieldViewModeAlways;

    UIView *paddingView1 = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, 0)];
    txt_Password.leftView = paddingView1;
    txt_Password.leftViewMode = UITextFieldViewModeAlways;
    
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    
    [txt_UserName.layer setCornerRadius:4.0];
    [txt_UserName.layer setBorderWidth:1.0];
    [txt_UserName.layer setBorderColor:[UIColor colorWithRed:158.0/255.0 green:151.0/255.0 blue:141.0/255.0 alpha:1.0].CGColor];
    
    [txt_Password.layer setCornerRadius:4.0];
    [txt_Password.layer setBorderWidth:1.0];
    [txt_Password.layer setBorderColor:[UIColor colorWithRed:158.0/255.0 green:151.0/255.0 blue:141.0/255.0 alpha:1.0].CGColor];
    
    [txtEmail.layer setCornerRadius:4.0];
    [txtEmail.layer setBorderWidth:1.0];
    [txtEmail.layer setBorderColor:[UIColor colorWithRed:158.0/255.0 green:151.0/255.0 blue:141.0/255.0 alpha:1.0].CGColor];
    
    
    txt_UserName.text = @"";
    txt_Password.text = @"";
    txtEmail.text = @"";
    
    //[btn_Login.layer setBorderWidth:2.0];
    [btn_Login.layer setCornerRadius:7.0];
    //[btn_Login.layer setBorderColor:[UIColor colorWithRed:235.0/255.0 green:135.0/255.0 blue:114.0/255.0 alpha:1.0].CGColor];
    
    [btnSend.layer setCornerRadius:7.0];
    
    [UIApplication sharedApplication].idleTimerDisabled = YES;
    
    viewSelectGuestMode.hidden = true;
    [viewSelectGuestMode setBackgroundColor:[[UIColor colorWithRed:199/255.0 green:196/255.0 blue:198/255.0 alpha:0.8] colorWithAlphaComponent:0.5]];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark -
#pragma mark - Text Field Delegate Methods

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    txtFieldRef = textField;
    
    txtFieldRef.keyboardType = UIKeyboardTypeDefault;
    
    [txtFieldRef reloadInputViews];
    
    if(textField == txtEmail)
    {
        _lblSigninToTop.constant = -130;
    }
    
    return YES;
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range
replacementString:(NSString *)string
{
    
    return YES;
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    NSInteger nextTag = textField.tag + 1;
    
    
    UIResponder *nextResponder = [textField.superview viewWithTag:nextTag];
    
    if (nextResponder)
    {
        
        
        CGSize screenSize = [[UIScreen mainScreen] bounds].size;
        
        if(screenSize.height == 480.0f)
        {
            
        }
        else
        {
            
        }
        
        
        [self.view setNeedsUpdateConstraints];
        
        [UIView animateWithDuration:0.40f animations:^{
            [self.view layoutIfNeeded];
        }];
        
        [nextResponder becomeFirstResponder];
    }
    else
    {
        CGSize screenSize = [[UIScreen mainScreen] bounds].size;
        
        if(screenSize.height == 480.0f)
        {
            
        }
        else
        {
            
        }
        
        _lblSigninToTop.constant = 70;
        
        [self.view setNeedsUpdateConstraints];
        
        [UIView animateWithDuration:0.40f animations:^{
            [self.view layoutIfNeeded];
        }];
        //lblMatchesFound.hidden= TRUE;
        [textField resignFirstResponder];
        return YES;
    }
    
    return NO;
}


#pragma mark -
#pragma mark - Custom Button Methods

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    for (UIView * txt in self.view.subviews){
        if ([txt isKindOfClass:[UITextField class]] && [txt isFirstResponder]) {
            [txt resignFirstResponder];
        }
    }
    
    _lblSigninToTop.constant = 70;
    
    CGSize screenSize = [[UIScreen mainScreen] bounds].size;
    
    if(screenSize.height == 480.0f)
    {
        
    }
    else
    {
        
    }
    
    [txtFieldRef resignFirstResponder];
    
    [self.view setNeedsUpdateConstraints];
    
    [UIView animateWithDuration:0.40f animations:^{
        [self.view layoutIfNeeded];
    }];
}

- (IBAction)btn_Login_Clicked:(id)sender
{
//    DetailsVC *objDVC = [[DetailsVC alloc] initWithNibName:@"DetailsVC" bundle:nil];
//     
//     [self.navigationController pushViewController:objDVC animated:YES];
    
    [txtFieldRef resignFirstResponder];
    
    if(![txt_UserName.text isEqualToString:@""] & ![txt_Password.text isEqualToString:@""])
    {
        
        if(appDelegate.isInternetReachble)
        {
            
            [appDelegate startActivityIndicator:self.view];
            self.view.userInteractionEnabled = TRUE;
            
            
            //NSString *urlStr = [NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/Rsnt/seam/resource/restv1/loginAuthRestAction/loginCredAuth?username=jkim@kyobee.com&password=jaekim"];
            
            //NSString *urlStr = [NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/Rsnt/seam/resource/restv1/loginAuthRestAction/loginCredAuth?username=%@&password=%@",txt_UserName.text,txt_Password.text];
            
            
            
            NSString *urlStr = [NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/kyobee/rest/loginCredAuth?username=%@&password=%@",txt_UserName.text,txt_Password.text];
            
            //NSString *urlStr = [NSString stringWithFormat:@"http://advantech.aksharnxdigital.com/kyobee/rest/loginCredAuth?username=%@&password=%@",txt_UserName.text,txt_Password.text];
            
            /*
             * URLWithString - Returns an NSURL object initialized with URLString.
             * If the URL string was malformed or nil, returns nil.
             */
            NSURL *url = [NSURL URLWithString:urlStr];
            
            /*
             * requestWithURL - Creates and returns a URL request for a specified URL with default cache policy (NSURLRequestUseProtocolCachePolicy) and timeout value (60 seconds).
             *
             */
            NSURLRequest *request = [NSURLRequest requestWithURL:url];
            
            [NSURLConnection sendAsynchronousRequest:request queue:[NSOperationQueue mainQueue] completionHandler:^(NSURLResponse *response, NSData *data, NSError *connectionError)
             {
                 self.view.userInteractionEnabled = TRUE;
                 [appDelegate stopActivityIndicator];
                 
                 if(data != nil)
                 {
                     NSString *htmlSTR = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
                     NSLog(@"%@",htmlSTR);
                     
                     NSData *data = [htmlSTR dataUsingEncoding:NSUTF8StringEncoding];
                     id json = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
                     
                     //NSDictionary *jsonObject=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:nil];
                     
                     if([[json valueForKey:@"success"] isEqualToString:@"0"])
                     {
                         NSMutableArray *seat = [[NSMutableArray alloc] initWithCapacity:0];
                         
                         
                         [[NSUserDefaults standardUserDefaults] setValue:[json valueForKey:@"OrgId"] forKey:@"OrgId"];
                         
                         if(![[json valueForKey:@"logofile name"] isEqual:[NSNull null]])
                         {
                             [[NSUserDefaults standardUserDefaults] setValue:[json valueForKey:@"logofile name"] forKey:@"logofile name"];
                         }
                         else
                         {
                             [[NSUserDefaults standardUserDefaults] setValue:@"" forKey:@"logofile name"];
                         }
                         
                         if(![[json valueForKey:@"smsRoute"] isEqual:[NSNull null]])
                         {
                             [[NSUserDefaults standardUserDefaults] setValue:[json valueForKey:@"smsRoute"] forKey:@"smsRoute"];
                         }
                         else
                         {
                             [[NSUserDefaults standardUserDefaults] setValue:@"" forKey:@"smsRoute"];
                         }
                         
                         
                         NSArray *seatArray = [json valueForKey:@"seatpref"];
                         
                         if(seatArray.count > 0)
                         {
                             [seat addObjectsFromArray:seatArray];
                             
                             [[NSUserDefaults standardUserDefaults]setValue:seat forKey:@"seatpref"];
                         }
                         
                         //
                         
                         if(btn_Remember.selected)
                         {
                             [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"rememberMe"];
                             [[NSUserDefaults standardUserDefaults]synchronize];
                         }
                         else
                         {
                             [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"rememberMe"];
                             [[NSUserDefaults standardUserDefaults]synchronize];
                         }
                         
                         
                         if(![[json valueForKey:@"clientBase"] isEqual:[NSNull null]])
                         {
                             [[NSUserDefaults standardUserDefaults] setValue:[json valueForKey:@"clientBase"] forKey:@"clientBase"];
                         }
                         else
                         {
                             [[NSUserDefaults standardUserDefaults] setValue:@"" forKey:@"clientBase"];
                         }
                          
                         
                         //[[NSUserDefaults standardUserDefaults] setValue:@"1" forKey:@"userthemepref"];
                         
                         //
                         
                         [[NSUserDefaults standardUserDefaults] synchronize];
                         
                         viewSelectGuestMode.hidden = false;
                         
                         /*DetailsVC *objDVC = [[DetailsVC alloc] initWithNibName:@"DetailsVC" bundle:nil];
                         [self.navigationController pushViewController:objDVC animated:YES];*/
                     }
                     else if([[json valueForKey:@"success"] isEqualToString:@"-1"])
                     {
                         UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:[json valueForKey:@"error"] delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                         
                         [alert show];
                     }
                     else if([[json valueForKey:@"success"] isEqualToNumber:[NSNumber numberWithInt:2]])
                     {
                         /*UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:[json valueForKey:@"message"] delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                          
                          [alert show];*/
                     }
                     else if([[json valueForKey:@"success"] isEqualToNumber:[NSNumber numberWithInt:-2]])
                     {
                         /*UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:[json valueForKey:@"message"] delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                          
                          [alert show];*/
                     }
                 }
             }];
        }
        else
        {
            UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:@"Please check your internet connection." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
            
            [alert show];
        }
    }
    else
    {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Kyobee" message:@"All fields are mandatory." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
        alert = nil;
    }
}

- (IBAction)btn_Fogot_Psw_Clicked:(id)sender
{
    btn_Fogot_Psw.selected = !btn_Fogot_Psw.selected;
    
    if(btn_Fogot_Psw.selected == true)
    {
        txtEmail.hidden = false;
        btnSend.hidden = false;
        
        txtEmail.text = @"";
    }
    else
    {
        txtEmail.hidden = true;
        btnSend.hidden = true;
    }
}

- (IBAction)btn_Remember_Clicked:(id)sender
{
    btn_Remember.selected = !btn_Remember.selected;
    
    if(btn_Remember.selected)
    {
        btn_Remember.selected = TRUE;
        //[[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"rememberMe"];
        //[[NSUserDefaults standardUserDefaults]synchronize];
    }
    else
    {
        btn_Remember.selected = FALSE;
        //[[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"rememberMe"];
        //[[NSUserDefaults standardUserDefaults]synchronize];
    }
}

- (IBAction)btnSend_clicked:(id)sender {
}
- (IBAction)btnCheckinMode_clicked:(id)sender
{
    [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"checkinmodeselected"];
    [[NSUserDefaults standardUserDefaults] synchronize];
    
    DetailsVC *objDVC = [[DetailsVC alloc] initWithNibName:@"DetailsVC" bundle:nil];
    [self.navigationController pushViewController:objDVC animated:YES];
}

- (IBAction)btnDisplayMode_clicked:(id)sender
{
    [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"displaymodeselected"];
    [[NSUserDefaults standardUserDefaults] synchronize];
    
    /*GuestListVC *guestListVC = [[GuestListVC alloc] initWithNibName:@"GuestListVC" bundle:nil];
    [self.navigationController pushViewController:guestListVC animated:YES];*/
    
    KyobeeGuestList *kyobeeGuestList = [[KyobeeGuestList alloc] initWithNibName:@"KyobeeGuestList" bundle:nil];
    [self.navigationController pushViewController:kyobeeGuestList animated:YES];
}

- (IBAction)btnPrivacyPolicy_clicked:(id)sender
{
    NSURL *URL = [NSURL URLWithString:@"http://kyobee.com/"];
    
    if ([[UIApplication sharedApplication] canOpenURL:URL]) {
        [[UIApplication sharedApplication] openURL:URL];
    }
}
@end
