//
//  DetailsVC.m
//  Kyobee
//
//  Created by Jenis Shah on 9/16/16.
//
//


/*
 
 Changed in class "OrtcClient.m" in method 
 
 - (void)opReceive:(NSString*) message
 
 */

#import "DetailsVC.h"

#import "SeatPrefCell.h"

///*** web image
#import "UIImageView+WebCache.h"
//****


@interface MyTextFieldDetails : UITextField

@property (nonatomic) IBInspectable CGFloat padding;


@end

@implementation MyTextFieldDetails

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

@interface DetailsVC ()

@property (nonatomic,strong) UILongPressGestureRecognizer *lpgr;

@end

@implementation DetailsVC

- (void)viewDidLoad
{
    appDelegate =(AppDelegate*)[[UIApplication sharedApplication]delegate];
    
    
    self.lpgr = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(handleLongPressGestures:)];
    self.lpgr.minimumPressDuration = 5.0f;
    self.lpgr.numberOfTouchesRequired = 2;
    self.lpgr.allowableMovement = 100.0f;
    
    //[btnLogout addGestureRecognizer:self.lpgr];
    [btnTopLeft addGestureRecognizer:self.lpgr];
    //[btnTopRight addGestureRecognizer:self.lpgr];
    //[btnBottomLeft addGestureRecognizer:self.lpgr];
    
    
    seatPrefArray = [[NSMutableArray alloc] initWithCapacity:0];
    selectedPref = [[NSMutableArray alloc] initWithCapacity:0];
    
    
    seatPrefArray = [[NSUserDefaults standardUserDefaults] valueForKey:@"seatpref"];
    
    self.navigationController.navigationBarHidden = TRUE;
    
    First_View.hidden = NO;
    form_View.hidden = YES;
    Thank_View.hidden = YES;

    UIView *paddingView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 15, 0)];
    txt_Name.leftView = paddingView;
    txt_Name.leftViewMode = UITextFieldViewModeAlways;
    
    UIView *paddingView1 = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 15, 0)];
    txt_your_party.leftView = paddingView1;
    txt_your_party.leftViewMode = UITextFieldViewModeAlways;
    
    UIView *paddingView2 = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 15, 0)];
    txt_Phone_Or_Mail.leftView = paddingView2;
    txt_Phone_Or_Mail.leftViewMode = UITextFieldViewModeAlways;
    
    btn_Cell_Phone.selected = TRUE;
    
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    // Realtime
    _ortcClient = [OrtcClient ortcClientWithConfig:self];
    [_ortcClient setClusterUrl:@"https://ortc-developers.realtime.co/server/ssl/2.1"];
    [_ortcClient connect:@"j9MLMa" authenticationToken:@"testToken"];
    //
    
    //[btnLogout.layer setBorderWidth:1.0];
    //[btnLogout.layer setCornerRadius:3.0];
    //[btnLogout.layer setBorderColor:[UIColor colorWithRed:92.0/255.0 green:94.0/255.0 blue:102.0/255.0 alpha:1.0].CGColor];
    
    /*NSTimer *timerforScrollView;
    timerforScrollView =[NSTimer scheduledTimerWithTimeInterval:0.1 target:self selector:@selector(forScrollView)userInfo:nil repeats:NO];*/
    
    _tableViewWB.backgroundColor = [UIColor clearColor];
    _tableViewWB.delegate = self;
    _tableViewWB.dataSource = self;
    
    //tblViewSeatPref = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, 80, self.view.frame.size.width) style:UITableViewStylePlain];
    
    
    [self addHorizontalTable];
}

- (void)handleLongPressGestures:(UILongPressGestureRecognizer *)sender
{
    if ([sender isEqual:self.lpgr])
    {
        if (sender.state == UIGestureRecognizerStateBegan)
        {
            [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"rememberMe"];
            
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"checkinmodeselected"];
            
            [[NSUserDefaults standardUserDefaults]synchronize];
            
            [self.navigationController popToRootViewControllerAnimated:YES];
        }
    }
}

- (void)addHorizontalTable
{
    UIInterfaceOrientation orientation = [UIApplication sharedApplication].statusBarOrientation;
    
    if (UIInterfaceOrientationIsLandscape(orientation))
    {
        //tblViewSeatPref.frame = CGRectMake(331, 456, 60, 614);
        tblViewSeatPref = [[UITableView alloc] initWithFrame:CGRectMake(610, 183, 60, 607) style:UITableViewStylePlain];
    }
    else
    {
        //tblViewSeatPref.frame = CGRectMake(331, 484, 60, 340);
        tblViewSeatPref = [[UITableView alloc] initWithFrame:CGRectMake(475, 344, 60, 340) style:UITableViewStylePlain];
    }
    
    tblViewSeatPref.separatorStyle = UITableViewCellSeparatorStyleNone;
    tblViewSeatPref.showsHorizontalScrollIndicator = false;
    tblViewSeatPref.showsVerticalScrollIndicator = false;
    
    tblViewSeatPref.backgroundColor = [UIColor clearColor];
    tblViewSeatPref.delegate = self;
    tblViewSeatPref.dataSource = self;
    tblViewSeatPref.transform = CGAffineTransformMakeRotation(-M_PI / 2);
    //tblViewSeatPref.center = CGPointMake(self.view.center.x, 40);
    
    
    //tblViewSeatPref.hidden = true;
    
    [form_View addSubview:tblViewSeatPref];
}

- (void) forScrollView
{
    [form_View addSubview:[self tableView]];
    _tableViewWB.showsHorizontalScrollIndicator = NO;
}

#pragma mark - getter
- (WBTableView *)tableView
{
    if (_tableViewWB == nil)
    {
        _tableViewWB = [[WBTableView alloc] init];
        //        _tableViewWB.frame = CGRectMake(24, 31, self.view.frame.size.width-14, 95);
        //CGSize screenSize = [[UIScreen mainScreen] bounds].size;
        
        /*_tableViewWB.frame = CGRectMake(331, 456, 614, 60);*/
        
        UIInterfaceOrientation orientation = [UIApplication sharedApplication].statusBarOrientation;
        
        if (UIInterfaceOrientationIsLandscape(orientation))
        {
            _tableViewWB.frame = CGRectMake(331, 456, 614, 60);
        }
        else
        {
            _tableViewWB.frame = CGRectMake(331, 484, 340, 60);
        }
        
        /*if(screenSize.height == 480)
        {
            _tableViewWB.frame = CGRectMake(viewMedia.frame.origin.x, 7, viewMedia.frame.size.width-30, 95);
        }
        if(screenSize.height == 568)
        {
            _tableViewWB.frame = CGRectMake(viewMedia.frame.origin.x, 7, viewMedia.frame.size.width-30, 95);
        }
        if(screenSize.height == 667)
        {
            //_tableViewWB.frame = CGRectMake(viewMedia.frame.origin.x, 7, viewMedia.frame.size.width+23, 95);
            
            _tableViewWB.frame = CGRectMake(viewMedia.frame.origin.x, 7, viewMedia.frame.size.width-30, 95);
        }
        if(screenSize.height == 736)
        {
            //_tableViewWB.frame = CGRectMake(viewMedia.frame.origin.x, 7, viewMedia.frame.size.width+63, 95);
            
            _tableViewWB.frame = CGRectMake(viewMedia.frame.origin.x, 7, viewMedia.frame.size.width-30, 95);
        }*/
        
        
        
        _tableViewWB.backgroundColor = [UIColor clearColor];
        _tableViewWB.delegate = self;
        _tableViewWB.dataSource = self;
    }
    else
    {
        UIInterfaceOrientation orientation = [UIApplication sharedApplication].statusBarOrientation;
        
        if (UIInterfaceOrientationIsLandscape(orientation))
        {
            _tableViewWB.frame = CGRectMake(331, 456, 614, 60);
        }
        else
        {
            _tableViewWB.frame = CGRectMake(331, 484, 340, 60);
        }
    }
    
    
    return _tableViewWB;
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    
    
    if([[[NSUserDefaults standardUserDefaults] valueForKey:@"smsRoute"]  isEqualToString:@""])
    {
        txt_Phone_Or_Mail.placeholder = @"+1 (___) ____-______ (Optional)";
        txt_Phone_Or_Mail.text = @"";
    }
    else
    {
        txt_Phone_Or_Mail.placeholder = @"+1 (___) ____-______*";
        txt_Phone_Or_Mail.text = @"";
    }
    
    
    [UIApplication sharedApplication].idleTimerDisabled = YES;
    
    if([[[NSUserDefaults standardUserDefaults] valueForKey:@"clientBase"]  isEqual:@"admin"])
    {
        lblClientBase.text = @"KYOBEE";
        
        //Home View
        lbl_Count.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        Lbl_waiting.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        Img_Standing_Line.backgroundColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        Lbl_Now_serving.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        Lbl_Now_serving_Count.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        Lbl_Est_Wait_Time.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        Lbl_Est_Time.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        [Btn_Press_Here_Checkin.layer setCornerRadius:8.0];
        Btn_Press_Here_Checkin.backgroundColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        lblHours.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        lblColon.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        lblMinutes.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        lblWelcome.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        lblFooter.backgroundColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        
        
        //Form View
        [Btn_Add_me_wait_list.layer setCornerRadius:8.0];
        Btn_Add_me_wait_list.backgroundColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        
        lblAdd_New_Guest_bg.backgroundColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        
        [Img_Required_Bg.layer setBorderColor:[UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0].CGColor];
        [Img_Required_Bg.layer setBorderWidth:3.0];
        [Img_Required_Bg.layer setCornerRadius:5.0];
        
        //Thank you View
        Lbl_Number.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        [Btn_Thank_you_ok.layer setCornerRadius:8.0];
        Btn_Thank_you_ok.backgroundColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        
        [Img_thank_you_Popup.layer setBorderColor:[UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0].CGColor];
        [Img_thank_you_Popup.layer setBorderWidth:3.0];
        [Img_thank_you_Popup.layer setCornerRadius:5.0];
        
    }
    else if([[[NSUserDefaults standardUserDefaults] valueForKey:@"clientBase"]  isEqual:@"advantech"])
    {
        lblClientBase.text = @"ADVANTECH";
        
        //Home View
        lbl_Count.textColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        Lbl_waiting.textColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        Img_Standing_Line.backgroundColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        Lbl_Now_serving.textColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        Lbl_Now_serving_Count.textColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        Lbl_Est_Wait_Time.textColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        Lbl_Est_Time.textColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        [Btn_Press_Here_Checkin.layer setCornerRadius:8.0];
        Btn_Press_Here_Checkin.backgroundColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        lblHours.textColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        lblColon.textColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        lblMinutes.textColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        lblWelcome.textColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        lblFooter.backgroundColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        
        
        //Form View
        [Btn_Add_me_wait_list.layer setCornerRadius:8.0];
        Btn_Add_me_wait_list.backgroundColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        
        lblAdd_New_Guest_bg.backgroundColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        
        [Img_Required_Bg.layer setBorderColor:[UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0].CGColor];
        [Img_Required_Bg.layer setBorderWidth:3.0];
        [Img_Required_Bg.layer setCornerRadius:5.0];
        
        //Thank you View
        Lbl_Number.textColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        [Btn_Thank_you_ok.layer setCornerRadius:8.0];
        Btn_Thank_you_ok.backgroundColor = [UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0];
        
        [Img_thank_you_Popup.layer setBorderColor:[UIColor colorWithRed:72.0/255.0 green:61.0/255.0 blue:139.0/255.0 alpha:1.0].CGColor];
        [Img_thank_you_Popup.layer setBorderWidth:3.0];
        [Img_thank_you_Popup.layer setCornerRadius:5.0];
    }
    else
    {
        lblClientBase.text = @"KYOBEE";
        
        //Home View
        lbl_Count.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        Lbl_waiting.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        Img_Standing_Line.backgroundColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        Lbl_Now_serving.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        Lbl_Now_serving_Count.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        Lbl_Est_Wait_Time.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        Lbl_Est_Time.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        [Btn_Press_Here_Checkin.layer setCornerRadius:8.0];
        Btn_Press_Here_Checkin.backgroundColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        lblHours.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        lblColon.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        lblMinutes.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        lblWelcome.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        lblFooter.backgroundColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        
        
        //Form View
        [Btn_Add_me_wait_list.layer setCornerRadius:8.0];
        Btn_Add_me_wait_list.backgroundColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        
        lblAdd_New_Guest_bg.backgroundColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        
        [Img_Required_Bg.layer setBorderColor:[UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0].CGColor];
        [Img_Required_Bg.layer setBorderWidth:3.0];
        [Img_Required_Bg.layer setCornerRadius:5.0];
        
        //Thank you View
        Lbl_Number.textColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        [Btn_Thank_you_ok.layer setCornerRadius:8.0];
        Btn_Thank_you_ok.backgroundColor = [UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0];
        
        [Img_thank_you_Popup.layer setBorderColor:[UIColor colorWithRed:225.0/255.0 green:75.0/255.0 blue:40.0/255.0 alpha:1.0].CGColor];
        [Img_thank_you_Popup.layer setBorderWidth:3.0];
        [Img_thank_you_Popup.layer setCornerRadius:5.0];
    }
    
    

    
    
    
    
    
    [txt_Name.layer setBorderWidth:1.5];
    [txt_Name.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
    
    [txt_Phone_Or_Mail.layer setBorderWidth:1.5];
    [txt_Phone_Or_Mail.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
    
    [txt_your_party.layer setBorderWidth:1.5];
    [txt_your_party.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
    
    [Img_seating_Bg.layer setBorderWidth:1.5];
    [Img_seating_Bg.layer setCornerRadius:3.0];
    [Img_seating_Bg.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
    
    [tblViewParties.layer setBorderWidth:1.5];
    [tblViewParties.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
    
    [self loadTimeMatrics];
    
    SpeedConnection = [NSTimer timerWithTimeInterval:60.0 target:self selector:@selector(startSpeedConnection) userInfo:nil repeats:YES];
    
    [[NSRunLoop mainRunLoop] addTimer:SpeedConnection forMode:NSDefaultRunLoopMode];
    
    UIInterfaceOrientation orientation = [UIApplication sharedApplication].statusBarOrientation;
    
    if (UIInterfaceOrientationIsLandscape(orientation))
    {
        // For Home View
        _nowServingToValue.constant = 0;
        _estTimeToValue.constant = 0;
        _btnCheckinToBottom.constant = 94;
        _partiesWaitingToVerticalLine.constant = 50;
        _nowServingTopAlign.constant = -37;
        _estTimeTopAlign.constant = -37;
        _btnCheckinToTop.constant = 49;
        //
        
        // Form View
        
        _formViewWidth.constant = 934;
        _formViewCenter.constant = 0;
        
        //
        
        
        // Thank You View
        
        _thankYouViewWidth.constant = 934;
        
        //
        
    }
    else
    {
        // For Home View
        _nowServingToValue.constant = 35;
        _estTimeToValue.constant = 35;
        _btnCheckinToBottom.constant = 144;
        _partiesWaitingToVerticalLine.constant = 90;
        _nowServingTopAlign.constant = -17;
        _estTimeTopAlign.constant = -17;
        _btnCheckinToTop.constant = 109;
        //
        
        // Form View
        
        _formViewWidth.constant = 678;
        _formViewCenter.constant = -100;
        
        //
        
        
        // Thank You View
        
        _thankYouViewWidth.constant = 678;
        
        //
        
    }
    
    [self performSelector:@selector(adjustScrollViewContentSize) withObject:nil afterDelay:0.1];
    
    [[NSNotificationCenter defaultCenter] addObserver:self  selector:@selector(orientationChanged:)    name:UIDeviceOrientationDidChangeNotification  object:nil];
}

-(void)adjustScrollViewContentSize
{
    _tableViewWB.contentSize = CGSizeMake(550, 60);
}

- (void)orientationChanged:(NSNotification *)notification
{
    [self adjustViewsForOrientation:[[UIApplication sharedApplication] statusBarOrientation]];
}

- (void) adjustViewsForOrientation:(UIInterfaceOrientation) orientation
{
    
    [tblViewSeatPref removeFromSuperview];
    
    if (UIInterfaceOrientationIsLandscape(orientation))
    {
        // For Home View
        _nowServingToValue.constant = 0;
        _estTimeToValue.constant = 0;
        _btnCheckinToBottom.constant = 94;
        _partiesWaitingToVerticalLine.constant = 50;
        _nowServingTopAlign.constant = -37;
        _estTimeTopAlign.constant = -37;
        _btnCheckinToTop.constant = 49;
        //
        
        // Form View
        
        _formViewWidth.constant = 934;
        _formViewCenter.constant = 0;
        
        
        //
        
        
        // Thank You View
        
        _thankYouViewWidth.constant = 934;
        
        //
        
        
        
    }
    else
    {
        // For Home View
        _nowServingToValue.constant = 35;
        _estTimeToValue.constant = 35;
        _btnCheckinToBottom.constant = 144;
        _partiesWaitingToVerticalLine.constant = 90;
        _nowServingTopAlign.constant = -17;
        _estTimeTopAlign.constant = -17;
        _btnCheckinToTop.constant = 109;
        //
        
        // Form View
        
        _formViewWidth.constant = 678;
        _formViewCenter.constant = -100;
        
        
        //
        
        
        // Thank You View
        
        _thankYouViewWidth.constant = 678;
        
        //
    }
    
    [self addHorizontalTable];
    
    //[self tableView];
    
    /*[self.view setNeedsUpdateConstraints];
    
    [UIView animateWithDuration:0.40f animations:^{
        [self.view layoutIfNeeded];
    }];*/
}

-(void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    [[NSNotificationCenter defaultCenter]removeObserver:self name:UIDeviceOrientationDidChangeNotification object:nil];
}

-(void)startSpeedConnection
{
    if(appDelegate.isInternetReachble)
    {
        
        //NSString *urlStr = [NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/Rsnt/seam/resource/restv1/loginAuthRestAction/loginCredAuth?username=jkim@kyobee.com&password=jaekim"];
        
        //NSString *urlStr = [NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/Rsnt/seam/resource/restv1/waitlistRestAction/totalwaittimemetricks"];
        
        NSString *urlStr = [NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/kyobee/rest/waitlistRestAction/totalwaittimemetricks"];
        
        
        
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
        
        timerInternet = [NSTimer timerWithTimeInterval:20.0 target:self selector:@selector(stopSpeedTest) userInfo:nil repeats:NO];
        
        [[NSRunLoop mainRunLoop] addTimer:timerInternet forMode:NSDefaultRunLoopMode];
        
        [NSURLConnection sendAsynchronousRequest:request queue:[NSOperationQueue mainQueue] completionHandler:^(NSURLResponse *response, NSData *data, NSError *connectionError)
         {
             self.view.userInteractionEnabled = TRUE;
             [appDelegate stopActivityIndicator];
             
             [timerInternet invalidate];
             timerInternet = nil;
             
             btnInternetConnection.hidden = true;
             
             if(data != nil)
             {
                 NSString *htmlSTR = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
                 //NSLog(@"%@",htmlSTR);
                 
                 NSData *data = [htmlSTR dataUsingEncoding:NSUTF8StringEncoding];
                 id json = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
                 
                 //NSDictionary *jsonObject=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:nil];
                 
                 if([[json valueForKey:@"success"] isEqualToString:@"1"])
                 {
                     
                 }
                 else if([[json valueForKey:@"success"] isEqualToString:@"-1"])
                 {
                     /*UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:[json valueForKey:@"error"] delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                      
                      [alert show];*/
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


-(void)stopSpeedTest
{
    btnInternetConnection.hidden = false;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:YES];
    
    
    
    //[self performSelector:@selector(handleLockButton) withObject:nil afterDelay:0];
}

- (IBAction)btnInternetConnection_clicked:(id)sender
{
    btnInternetConnection.hidden = true;
}

- (IBAction)btn_Phone_Or_Mail_Clicked:(id)sender
{
    btn_Cell_Phone.selected = !btn_Cell_Phone.selected;
    
    if(btn_Cell_Phone.selected)
    {
        btn_Cell_Phone.selected = TRUE;
        btn_Email.selected = FALSE;
        
        if([[[NSUserDefaults standardUserDefaults] valueForKey:@"smsRoute"]  isEqualToString:@""])
        {
            txt_Phone_Or_Mail.placeholder = @"+1 (___) ____-______ (Optional)";
            txt_Phone_Or_Mail.text = @"";
        }
        else
        {
            txt_Phone_Or_Mail.placeholder = @"+1 (___) ____-______*";
            txt_Phone_Or_Mail.text = @"";
        }
        
        
        
        
        txt_Phone_Or_Mail.keyboardType = UIKeyboardTypePhonePad;
        txt_Phone_Or_Mail.autocorrectionType = UITextAutocorrectionTypeNo;
        
        [txt_Phone_Or_Mail reloadInputViews];
       
    }
    else
    {
        btn_Cell_Phone.selected = FALSE;
        btn_Email.selected = TRUE;
        
        if([[[NSUserDefaults standardUserDefaults] valueForKey:@"smsRoute"]  isEqualToString:@""])
        {
            txt_Phone_Or_Mail.placeholder = @"E-Mail (Optional)";
            txt_Phone_Or_Mail.text = @"";
        }
        else
        {
            txt_Phone_Or_Mail.placeholder = @"E-Mail*";
            txt_Phone_Or_Mail.text = @"";
        }
        
        
        
        txt_Phone_Or_Mail.keyboardType = UIKeyboardTypeEmailAddress;
        txt_Phone_Or_Mail.autocorrectionType = UITextAutocorrectionTypeNo;
        
        [txt_Phone_Or_Mail reloadInputViews];
    }
    
    
}

- (IBAction)Btn_Close_Clicked:(id)sender
{
    form_View.hidden = YES;
    
    [txtFieldRef resignFirstResponder];
}

- (IBAction)Btn_Press_Here_Checkin_Clicked:(id)sender
{
    form_View.hidden = NO;
    
    txt_Name.text = @"";
    txt_Phone_Or_Mail.text = @"";
    txt_your_party.text = @"";
    
    Btn_Promotions_Specials.selected = false;
    
    btn_Cell_Phone.selected = true;
    
    if([[[NSUserDefaults standardUserDefaults] valueForKey:@"smsRoute"]  isEqualToString:@""])
    {
        txt_Phone_Or_Mail.placeholder = @"+1 (___) ____-______ (Optional)";
        txt_Phone_Or_Mail.text = @"";
    }
    else
    {
        txt_Phone_Or_Mail.placeholder = @"+1 (___) ____-______*";
        txt_Phone_Or_Mail.text = @"";
    }
    
    
    btn_Email.selected = false;
    
    if(selectedPref.count > 0)
        [selectedPref removeAllObjects];
    
    [tblViewSeatPref reloadData];
    
    [txt_Name.layer setBorderWidth:1.5];
    [txt_Name.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
    
    [txt_Phone_Or_Mail.layer setBorderWidth:1.5];
    [txt_Phone_Or_Mail.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
    
    [txt_your_party.layer setBorderWidth:1.5];
    [txt_your_party.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
    
    [_tableViewWB reloadData];
    
    tblViewParties.hidden = true;
}

- (IBAction)Btn_Patio_Clicked:(id)sender
{
    Btn_Patio.selected = !Btn_Patio.selected;
    
    if(Btn_Patio.selected)
    {
        Btn_Patio.selected = TRUE;
    }
    else
    {
        Btn_Patio.selected = FALSE;
    }
}
- (IBAction)Btn_Bar_Clicked:(id)sender
{
    Btn_Bar.selected = !Btn_Bar.selected;
    
    if(Btn_Bar.selected)
    {
        Btn_Bar.selected = TRUE;
    }
    else
    {
        Btn_Bar.selected = FALSE;
    }
}
- (IBAction)Btn_First_Available_Clicked:(id)sender
{
    Btn_First_Available.selected = !Btn_First_Available.selected;
    
    if(Btn_First_Available.selected)
    {
        Btn_First_Available.selected = TRUE;
    }
    else
    {
        Btn_First_Available.selected = FALSE;
    }
}
- (IBAction)Btn_Booth_Clicked:(id)sender
{
    Btn_Booth.selected = !Btn_Booth.selected;
 
    if(Btn_Booth.selected)
    {
        Btn_Booth.selected = TRUE;
    }
    else
    {
        Btn_Booth.selected = FALSE;
    }
}

- (IBAction)Btn_Table_Clicked:(id)sender
{
    Btn_Table.selected = !Btn_Table.selected;
    
    if(Btn_Table.selected)
    {
        Btn_Table.selected = TRUE;
    }
    else
    {
        Btn_Table.selected = FALSE;
    }
}

- (IBAction)btnParties_clicked:(id)sender
{
    tblViewParties.hidden = false;
    [tblViewParties reloadData];
    
    
    Lbl_Required.hidden = false;
    Btn_Close.hidden = false;
    _txtNameToTop.constant = -181;
    
    
    [txtFieldRef resignFirstResponder];
    
    [self.view setNeedsUpdateConstraints];
    
    [UIView animateWithDuration:0.40f animations:^{
        [self.view layoutIfNeeded];
    }];
    
    [self hideKeyBoard];
}

- (IBAction)Btn_Promotions_Specials_Clicked:(id)sender
{
    Btn_Promotions_Specials.selected = !Btn_Promotions_Specials.selected;
    
    if(Btn_Promotions_Specials.selected)
    {
        Btn_Promotions_Specials.selected = TRUE;
    }
    else
    {
        Btn_Promotions_Specials.selected = FALSE;
    }
}

#pragma mark - WBTableViewDataSource
- (NSInteger)numberOfItemsInTableView:(WBTableView *)tableView
{
    return  seatPrefArray.count;
}

- (CGFloat)tableView:(WBTableView *)tableView widthForItemAtIndex:(NSInteger)index
{
    if([[[seatPrefArray objectAtIndex:index] valueForKey:@"prefValue"] isEqualToString:@"First Available"])
    {
        return 185.0f;
    }
    else if([[[seatPrefArray objectAtIndex:index] valueForKey:@"prefValue"] isEqualToString:@"Patio"])
    {
        return 110.0f;
    }
    else if([[[seatPrefArray objectAtIndex:index] valueForKey:@"prefValue"] isEqualToString:@"Bar"])
    {
        return 90.0f;
    }
    else if([[[seatPrefArray objectAtIndex:index] valueForKey:@"prefValue"] isEqualToString:@"Booth"])
    {
        return 110.0f;
    }
    else
    {
        return 110.0f;
    }
}

- (UITableViewCell *)tableView:(WBTableView *)tableView cellForItemAtIndex:(NSInteger)index
{
    static NSString *CellIdentifier = @"Cell";
    
    SeatPrefCell *cell = (SeatPrefCell *)[tblViewSeatPref dequeueReusableCellWithIdentifier:CellIdentifier];
    
    //if (cell == nil)
    {
        cell = [[[NSBundle mainBundle]loadNibNamed:NSStringFromClass([SeatPrefCell class]) owner:nil options:nil] lastObject];
    }
    
    
    [[cell.btnSeatPref imageView]setContentMode:UIViewContentModeScaleAspectFit];
    
    /*[cell.btnSeatPref addTarget:self action:@selector(btnSeatCheck:) forControlEvents:UIControlEventTouchUpInside];
    
    [cell.btnSeatPref setTag:index];*/
    
    [cell.btnSeatPref addTarget:self action:@selector(btnPressed:) forControlEvents:UIControlEventTouchUpInside];
    [cell.btnSeatPref setTag:index];
    
    [cell.btnPrefOnLabel addTarget:self action:@selector(btnPressed:) forControlEvents:UIControlEventTouchUpInside];
    [cell.btnPrefOnLabel setTag:index];
    
    
    cell.lblSeatPref.text = [[seatPrefArray objectAtIndex:index] valueForKey:@"prefValue"];
    
    cell.lblSeatPref.textColor = [UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0];
    
    cell.lblSeatPref.font = [UIFont boldSystemFontOfSize:20.0];
    
    cell.lblSaperator.backgroundColor = [UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0];
    cell.lblSaperator.hidden = true;
    
    
    if([selectedPref count] > 0)
    {
        for(int i=0; i<[selectedPref count];i++)
        {
            
            if([[[selectedPref objectAtIndex:i] valueForKey:@"prefValueId"] isEqualToNumber:[[seatPrefArray objectAtIndex:index] valueForKey:@"prefValueId"]])
            {
                cell.btnSeatPref.selected = TRUE;
            }
            /*else
             {
             cell.btnFriend.selected = FALSE;
             }*/
        }
    }
    else
    {
        cell.btnSeatPref.selected = FALSE;
    }
    
    
    
    cell.backgroundColor = [UIColor clearColor];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    return cell;
}

#pragma mark - WBTableViewDelegate

- (void)tableView:(WBTableView *)tableView didSelectItemAtIndex:(NSInteger)index
{
    NSLog(@"------%ld",(long)index);
    
    if([selectedPref count] > 0)
    {
        for(int i=0; i<[selectedPref count];i++)
        {
            if([[[selectedPref objectAtIndex:i] valueForKey:@"prefValueId"] isEqualToNumber:[[seatPrefArray objectAtIndex:index] valueForKey:@"prefValueId"]])
            {
                [selectedPref removeObjectAtIndex:i];
                [tblViewSeatPref reloadData];
                
                return;
            }
            
        }
        
        for(int i=0; i<[selectedPref count];i++)
        {
            if(![[[selectedPref objectAtIndex:i] valueForKey:@"prefValueId"] isEqualToNumber:[[seatPrefArray objectAtIndex:index] valueForKey:@"prefValueId"]])
            {
                NSMutableDictionary *dict = [[NSMutableDictionary alloc] initWithCapacity:0];
                [dict setValue:[[seatPrefArray objectAtIndex:index] valueForKey:@"prefValueId"] forKey:@"prefValueId"];
                [dict setValue:[[seatPrefArray objectAtIndex:index] valueForKey:@"prefValue"] forKey:@"prefValue"];
                
                
                //[selectedCategories addObject:[[tempArray objectAtIndex:indexPath.row] valueForKey:@"categoryId"]];
                
                [selectedPref addObject:dict];
                
                [tblViewSeatPref reloadData];
                
                return;
            }
        }
    }
    else
    {
        
        NSMutableDictionary *dict = [[NSMutableDictionary alloc] initWithCapacity:0];
        [dict setValue:[[seatPrefArray objectAtIndex:index] valueForKey:@"prefValueId"] forKey:@"prefValueId"];
        [dict setValue:[[seatPrefArray objectAtIndex:index] valueForKey:@"prefValue"] forKey:@"prefValue"];
        
        [selectedPref addObject:dict];
        [tblViewSeatPref reloadData];
        
        //[selectedCategories addObject:[[tempArray objectAtIndex:indexPath.row] valueForKey:@"categoryId"]];
    }
    
}

-(void)btnPressed:(UIButton*)btn
{
    //NSLog(@"%ld",(long)btn.tag);
    
    if([selectedPref count] > 0)
    {
        for(int i=0; i<[selectedPref count];i++)
        {
            if([[[selectedPref objectAtIndex:i] valueForKey:@"prefValueId"] isEqualToNumber:[[seatPrefArray objectAtIndex:btn.tag] valueForKey:@"prefValueId"]])
            {
                [selectedPref removeObjectAtIndex:i];
                [_tableViewWB reloadData];
                [tblViewSeatPref reloadData];
                
                
                return;
            }
            
        }
        
        for(int i=0; i<[selectedPref count];i++)
        {
            if(![[[selectedPref objectAtIndex:i] valueForKey:@"prefValueId"] isEqualToNumber:[[seatPrefArray objectAtIndex:btn.tag] valueForKey:@"prefValueId"]])
            {
                NSMutableDictionary *dict = [[NSMutableDictionary alloc] initWithCapacity:0];
                [dict setValue:[[seatPrefArray objectAtIndex:btn.tag] valueForKey:@"prefValueId"] forKey:@"prefValueId"];
                [dict setValue:[[seatPrefArray objectAtIndex:btn.tag] valueForKey:@"prefValue"] forKey:@"prefValue"];
                
                
                //[selectedCategories addObject:[[tempArray objectAtIndex:indexPath.row] valueForKey:@"categoryId"]];
                
                [selectedPref addObject:dict];
                
                [_tableViewWB reloadData];
                
                [tblViewSeatPref reloadData];
                
                return;
            }
        }
    }
    else
    {
        
        NSMutableDictionary *dict = [[NSMutableDictionary alloc] initWithCapacity:0];
        [dict setValue:[[seatPrefArray objectAtIndex:btn.tag] valueForKey:@"prefValueId"] forKey:@"prefValueId"];
        [dict setValue:[[seatPrefArray objectAtIndex:btn.tag] valueForKey:@"prefValue"] forKey:@"prefValue"];
        
        [selectedPref addObject:dict];
        [_tableViewWB reloadData];
        [tblViewSeatPref reloadData];
        
        
        //[selectedCategories addObject:[[tempArray objectAtIndex:indexPath.row] valueForKey:@"categoryId"]];
    }
    
}



/*- (NSInteger)tableView:(PTEHorizontalTableView *)horizontalTableView
 numberOfRowsInSection:(NSInteger)section
{
    if(tblViewParties.hidden == true)
    {
        return  seatPrefArray.count;
    }
    else
    {
        return 23;
    }
}*/

/*- (UITableViewCell *)tableView:(PTEHorizontalTableView *)horizontalTableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if(tblViewParties.hidden == true)
    {
        
        static NSString *CellIdentifier = @"Cell";
        
        SeatPrefCell *cell = (SeatPrefCell *)[tblViewSeatPref dequeueReusableCellWithIdentifier:CellIdentifier];
        
        if (cell == nil)
        {
            cell = [[[NSBundle mainBundle]loadNibNamed:NSStringFromClass([SeatPrefCell class]) owner:nil options:nil] lastObject];
            cell.contentView.transform = CGAffineTransformMakeRotation(M_PI /2);
        }
        
        
        [[cell.btnSeatPref imageView]setContentMode:UIViewContentModeScaleAspectFit];
        
        [cell.btnSeatPref addTarget:self action:@selector(btnPressed:) forControlEvents:UIControlEventTouchUpInside];
        [cell.btnSeatPref setTag:indexPath.row];
        
        [cell.btnPrefOnLabel addTarget:self action:@selector(btnPressed:) forControlEvents:UIControlEventTouchUpInside];
        [cell.btnPrefOnLabel setTag:indexPath.row];
        
        cell.btnSeatPref.userInteractionEnabled = false;
        cell.btnPrefOnLabel.userInteractionEnabled = false;
        
        
        cell.lblSeatPref.text = [[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValue"];
        
        cell.lblSeatPref.textColor = [UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0];
        
        cell.lblSeatPref.font = [UIFont boldSystemFontOfSize:20.0];
        
        cell.lblSaperator.backgroundColor = [UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0];
        cell.lblSaperator.hidden = true;
        
        
        if([selectedPref count] > 0)
        {
            for(int i=0; i<[selectedPref count];i++)
            {
                
                if([[[selectedPref objectAtIndex:i] valueForKey:@"prefValueId"] isEqualToNumber:[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValueId"]])
                {
                    cell.btnSeatPref.selected = TRUE;
                }
                //else
                // {
                 //cell.btnFriend.selected = FALSE;
                // }
            }
        }
        else
        {
            cell.btnSeatPref.selected = FALSE;
        }
        
        
        
        cell.backgroundColor = [UIColor clearColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        return cell;
        
    }
    else
    {
        static NSString *cellIdentifier = @"Cell";
        UITableViewCell *cell = [tblViewParties dequeueReusableCellWithIdentifier:cellIdentifier];
        if (!cell)
        {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
        }
        
        if(indexPath.row == 0)
        {
            cell.textLabel.text =[NSString stringWithFormat:@"# of people in your party*"];
        }
        else
        {
            cell.textLabel.text =[NSString stringWithFormat:@"%ld",indexPath.row] ;
        }
        cell.backgroundColor = [UIColor clearColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.textLabel.textColor = [UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0];
        cell.textLabel.font = [UIFont boldSystemFontOfSize:20.0];
        
        return cell;
        
    }
    
}*/

/*- (CGFloat)tableView:(PTEHorizontalTableView *)horizontalTableView widthForCellAtIndexPath:(NSIndexPath *)indexPath
{
    if(tblViewParties.hidden == true)
    {
        if([[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValue"] isEqualToString:@"First Available"])
        {
            return 185.0f;
        }
        else if([[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValue"] isEqualToString:@"Patio"])
        {
            return 110.0f;
        }
        else if([[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValue"] isEqualToString:@"Bar"])
        {
            return 90.0f;
        }
        else if([[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValue"] isEqualToString:@"Booth"])
        {
            return 110.0f;
        }
        else
        {
            return 110.0f;
        }
    }
    else
    {
        return 30;
    }
}*/



/*- (void)tableView:(PTEHorizontalTableView *)horizontalTableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(tblViewParties.hidden == true)
    {
        
        if([selectedPref count] > 0)
         {
         for(int i=0; i<[selectedPref count];i++)
         {
         if([[[selectedPref objectAtIndex:i] valueForKey:@"prefValueId"] isEqualToNumber:[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValueId"]])
         {
         [selectedPref removeObjectAtIndex:i];
         [horizontalTableView.tableView reloadData];
         
         return;
         }
         
         }
         
         for(int i=0; i<[selectedPref count];i++)
         {
         if(![[[selectedPref objectAtIndex:i] valueForKey:@"prefValueId"] isEqualToNumber:[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValueId"]])
         {
         NSMutableDictionary *dict = [[NSMutableDictionary alloc] initWithCapacity:0];
         [dict setValue:[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValueId"] forKey:@"prefValueId"];
         [dict setValue:[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValue"] forKey:@"prefValue"];
         
         
         //[selectedCategories addObject:[[tempArray objectAtIndex:indexPath.row] valueForKey:@"categoryId"]];
         
         [selectedPref addObject:dict];
         
         [horizontalTableView.tableView reloadData];
         
         return;
         }
         }
         }
         else
         {
         
         NSMutableDictionary *dict = [[NSMutableDictionary alloc] initWithCapacity:0];
         [dict setValue:[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValueId"] forKey:@"prefValueId"];
         [dict setValue:[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValue"] forKey:@"prefValue"];
         
         [selectedPref addObject:dict];
         [horizontalTableView.tableView reloadData];
         
         //[selectedCategories addObject:[[tempArray objectAtIndex:indexPath.row] valueForKey:@"categoryId"]];
         }
    }
    else
    {
        if(indexPath.row > 0)
        {
            txt_your_party.text = [NSString stringWithFormat:@"%ld",indexPath.row];
            [tblViewParties setHidden:true];
            [tblViewSeatPref reloadData];
            
            [txt_your_party.layer setBorderWidth:1.5];
            [txt_your_party.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
        }
    }
}*/



#pragma mark - UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(tblViewParties.hidden == true)
    {
        return  seatPrefArray.count;
    }
    else
    {
        return 23;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(tblViewParties.hidden == true)
    {
        if([[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValue"] isEqualToString:@"First Available"])
        {
            return 185.0f;
        }
        else if([[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValue"] isEqualToString:@"Patio"])
        {
            return 110.0f;
        }
        else if([[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValue"] isEqualToString:@"Bar"])
        {
            return 90.0f;
        }
        else if([[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValue"] isEqualToString:@"Booth"])
        {
            return 110.0f;
        }
        else
        {
            return 110.0f;
        }
    }
    else
    {
        return 30;
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if(tblViewParties.hidden == true)
    {
        
        static NSString *CellIdentifier = @"Cell";
        
        SeatPrefCell *cell = (SeatPrefCell *)[tblViewSeatPref dequeueReusableCellWithIdentifier:CellIdentifier];
        
        if (cell == nil)
        {
            cell = [[[NSBundle mainBundle]loadNibNamed:NSStringFromClass([SeatPrefCell class]) owner:nil options:nil] lastObject];
            //cell.contentView.transform = CGAffineTransformMakeRotation(M_PI * 3/2);
            cell.contentView.transform = CGAffineTransformMakeRotation(M_PI /2);
            
        }
        
        
        [[cell.btnSeatPref imageView]setContentMode:UIViewContentModeScaleAspectFit];
        
        [cell.btnSeatPref addTarget:self action:@selector(btnPressed:) forControlEvents:UIControlEventTouchUpInside];
        [cell.btnSeatPref setTag:indexPath.row];
        
        [cell.btnPrefOnLabel addTarget:self action:@selector(btnPressed:) forControlEvents:UIControlEventTouchUpInside];
        [cell.btnPrefOnLabel setTag:indexPath.row];
        
        
        cell.lblSeatPref.text = [[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValue"];
        
        cell.lblSeatPref.textColor = [UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0];
        
        cell.lblSeatPref.font = [UIFont boldSystemFontOfSize:20.0];
        
        cell.lblSaperator.backgroundColor = [UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0];
        cell.lblSaperator.hidden = true;
        
        
        if([selectedPref count] > 0)
        {
            for(int i=0; i<[selectedPref count];i++)
            {
                
                if([[[selectedPref objectAtIndex:i] valueForKey:@"prefValueId"] isEqualToNumber:[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValueId"]])
                {
                    cell.btnSeatPref.selected = TRUE;
                }
                //else
                 //{
                 //cell.btnFriend.selected = FALSE;
                 //}
            }
        }
        else
        {
            cell.btnSeatPref.selected = FALSE;
        }
        
        
        
        cell.backgroundColor = [UIColor clearColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        return cell;
        
    }
    else
    {
        static NSString *cellIdentifier = @"Cell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
        if (!cell)
        {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
        }
        
        if(indexPath.row == 0)
        {
            cell.textLabel.text =[NSString stringWithFormat:@"# of people in your party*"];
        }
        else
        {
            cell.textLabel.text =[NSString stringWithFormat:@"%ld",indexPath.row] ;
        }
        cell.backgroundColor = [UIColor clearColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.textLabel.textColor = [UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0];
        cell.textLabel.font = [UIFont boldSystemFontOfSize:20.0];
        
        return cell;
            
    }
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(tblViewParties.hidden == true)
    {
        
        /*if([selectedPref count] > 0)
        {
            for(int i=0; i<[selectedPref count];i++)
            {
                if([[[selectedPref objectAtIndex:i] valueForKey:@"prefValueId"] isEqualToNumber:[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValueId"]])
                {
                    [selectedPref removeObjectAtIndex:i];
                    [tblViewSeatPref reloadData];
                    
                    return;
                }
                
            }
            
            for(int i=0; i<[selectedPref count];i++)
            {
                if(![[[selectedPref objectAtIndex:i] valueForKey:@"prefValueId"] isEqualToNumber:[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValueId"]])
                {
                    NSMutableDictionary *dict = [[NSMutableDictionary alloc] initWithCapacity:0];
                    [dict setValue:[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValueId"] forKey:@"prefValueId"];
                    [dict setValue:[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValue"] forKey:@"prefValue"];
                    
                    
                    //[selectedCategories addObject:[[tempArray objectAtIndex:indexPath.row] valueForKey:@"categoryId"]];
                    
                    [selectedPref addObject:dict];
                    
                    [tblViewSeatPref reloadData];
                    
                    return;
                }
            }
        }
        else
        {
            
            NSMutableDictionary *dict = [[NSMutableDictionary alloc] initWithCapacity:0];
            [dict setValue:[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValueId"] forKey:@"prefValueId"];
            [dict setValue:[[seatPrefArray objectAtIndex:indexPath.row] valueForKey:@"prefValue"] forKey:@"prefValue"];
            
            [selectedPref addObject:dict];
            [tblViewSeatPref reloadData];
            
            //[selectedCategories addObject:[[tempArray objectAtIndex:indexPath.row] valueForKey:@"categoryId"]];
        }*/
    }
    else
    {
        if(indexPath.row > 0)
        {
            txt_your_party.text = [NSString stringWithFormat:@"%ld",indexPath.row];
            [tblViewParties setHidden:true];
            [tblViewSeatPref reloadData];
            
            [txt_your_party.layer setBorderWidth:1.5];
            [txt_your_party.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
        }
    }
}

-(void)scrollViewWillBeginDragging:(UIScrollView *)scrollView
{
    if(scrollView == tblViewSeatPref)
    {
        tblViewParties.hidden = true;
    }
    else
    {
        
    }
}

-(void)btnSeatCheck:(id)sender
{
    
}


- (IBAction)Btn_Add__me_wait_list_Clicked:(id)sender
{
    
    
    if(appDelegate.isInternetReachble)
    {
        //if(![txt_Name.text isEqualToString:@""] & ![txt_Phone_Or_Mail.text isEqualToString:@""] & ![txt_your_party.text isEqualToString:@""])
        if(![txt_Name.text isEqualToString:@""] & ![txt_your_party.text isEqualToString:@""])
        {
            ///
            
            if([[[NSUserDefaults standardUserDefaults] valueForKey:@"smsRoute"]  isEqualToString:@""])
            {
                
            }
            else
            {
                if([txt_Phone_Or_Mail.text isEqualToString:@""])
                {
                    [txt_Phone_Or_Mail.layer setBorderWidth:1.5];
                    [txt_Phone_Or_Mail.layer setBorderColor:[UIColor redColor].CGColor];
                    
                    return;
                }
                else
                {
                    [txt_Phone_Or_Mail.layer setBorderWidth:1.5];
                    [txt_Phone_Or_Mail.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
                }
            }
            
            Lbl_Required.hidden = false;
            Btn_Close.hidden = false;
            _txtNameToTop.constant = -181;
            
            [txtFieldRef resignFirstResponder];
            
            [self.view setNeedsUpdateConstraints];
            
            [UIView animateWithDuration:0.40f animations:^{
                [self.view layoutIfNeeded];
            }];
            
            ///
            
            [txt_Name.layer setBorderWidth:1.5];
            [txt_Name.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
            [txt_Phone_Or_Mail.layer setBorderWidth:1.5];
            [txt_Phone_Or_Mail.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
            [txt_your_party.layer setBorderWidth:1.5];
            [txt_your_party.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
            
            
            if(selectedPref.count == 0)
            {
                UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:@"Please select seating preference." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                
                [alert show];
                
                return;
            }
            
            
            [appDelegate startActivityIndicator:self.view];
            self.view.userInteractionEnabled = TRUE;
            
            NSError *error;
            NSData *jsonData = [NSJSONSerialization dataWithJSONObject:selectedPref options:NSJSONWritingPrettyPrinted error:&error];
            NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
            NSData *data = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
            id json = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
            
            
            NSString *optIn;
            
            if(Btn_Promotions_Specials.selected)
            {
                optIn = @"true";
            }
            else
            {
                optIn = @"false";
            }
            
            NSString *number = [self formatIdentificationNumber:[NSString stringWithFormat:@"%@",txt_Phone_Or_Mail.text]];
            
            NSString *postString;
            
            NSData* jsonDataReq;
            
            if(btn_Cell_Phone.selected)
            {
                NSDictionary *newDatasetInfo = [NSDictionary dictionaryWithObjectsAndKeys:txt_Name.text, @"name", @"test", @"note",[[NSUserDefaults standardUserDefaults] valueForKey:@"OrgId"], @"organizationID",txt_your_party.text, @"noOfPeople",@"SMS", @"prefType",number, @"sms",optIn, @"optin",@"CHECKIN", @"status",json, @"guestPreferences", nil];
                
                //convert object to data
                jsonDataReq = [NSJSONSerialization dataWithJSONObject:newDatasetInfo options:kNilOptions error:&error];
                
                NSString *htmlSTR = [[NSString alloc] initWithData:jsonDataReq encoding:NSUTF8StringEncoding];
                NSLog(@"%@",htmlSTR);
            }
            else
            {
                
                NSDictionary *newDatasetInfo = [NSDictionary dictionaryWithObjectsAndKeys:txt_Name.text, @"name", @"test", @"note",[[NSUserDefaults standardUserDefaults] valueForKey:@"OrgId"], @"organizationID",txt_your_party.text, @"noOfPeople",@"EMAIL", @"prefType",txt_Phone_Or_Mail.text, @"email",optIn, @"optin",@"CHECKIN", @"status",json, @"guestPreferences", nil];
                
                //convert object to data
                jsonDataReq = [NSJSONSerialization dataWithJSONObject:newDatasetInfo options:kNilOptions error:&error];
                
                NSString *htmlSTR = [[NSString alloc] initWithData:jsonDataReq encoding:NSUTF8StringEncoding];
                NSLog(@"%@",htmlSTR);
            }
            
            
            //NSURL *aUrl = [NSURL URLWithString:[NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/Rsnt/seam/resource/restv1/waitlistRestAction/addGuest"]];
            
            NSURL *aUrl = [NSURL URLWithString:[NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/kyobee/web/rest/waitlistRestAction/addGuest"]];
            
            
            
            //NSLog(@"Ping:- %@",postString);
            NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:aUrl cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:60.0];
            
            [request setHTTPMethod:@"POST"];
            
            NSData *parameterData = [postString dataUsingEncoding:NSUTF8StringEncoding allowLossyConversion:YES];
            NSString *postLength = [NSString stringWithFormat:@"%lu", (unsigned long)[parameterData length]];
            
            [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
            [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
            [request setValue:@"application/json" forHTTPHeaderField:@"Accept"];
            [request setHTTPBody:jsonDataReq];
            
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
                     
                     //if([[json valueForKey:@"OP"] isEqualToString:@"ADD"])
                     if([[json valueForKey:@"status"] isEqualToString:@"SUCCESS"])
                     {
                         lbl_Count.text = [NSString stringWithFormat:@"(%@)",[[json valueForKey:@"serviceResult"]valueForKey:@"totalPartiesWaiting"]];
                         Lbl_Now_serving_Count.text = [NSString stringWithFormat:@"#%@",[[json valueForKey:@"serviceResult"]valueForKey:@"nowServingParty"]];
                         
                         NSString *minutes = [NSString stringWithFormat:@"%@",[[json valueForKey:@"serviceResult"]valueForKey:@"totalWaitTime"]];
                         int hour = [minutes intValue] / 60;
                         int min = [minutes intValue] % 60;
                         NSString *timeString = [NSString stringWithFormat:@"%.2d⁚%02d", hour, min];
                         Lbl_Est_Time.text = [NSString stringWithFormat:@"%@",timeString];
                         
                         lblHours.text = [NSString stringWithFormat:@"%.2d",hour];
                         lblMinutes.text = [NSString stringWithFormat:@"%02d",min];
                         lblColon.hidden = false;
                         
                         Lbl_Number.text = [NSString stringWithFormat:@"#%@",[[json valueForKey:@"serviceResult"]valueForKey:@"guestRank"]];
                         
                         form_View.hidden = YES;
                         Thank_View.hidden = NO;
                     }
                     else if([[json valueForKey:@"success"] isEqualToNumber:[NSNumber numberWithInt:-1]])
                     {
                         /*UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:[json valueForKey:@"message"] delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                          
                          [alert show];*/
                     }
                     else if([[json valueForKey:@"success"] isEqualToNumber:[NSNumber numberWithInt:2]])
                     {
                         /*UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:[json valueForKey:@"message"] delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                          
                          [alert show];*/
                     }
                     else if([[json valueForKey:@"success"] isEqualToNumber:[NSNumber numberWithInt:-2]])
                     {
                         UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:[json valueForKey:@"message"] delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                         
                         [alert show];
                     }
                 }
             }];
        }
        else
        {
            /*UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:@"All fields are mandatory." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
            
            [alert show];*/
            
            if([txt_Name.text isEqualToString:@""])
            {
                [txt_Name.layer setBorderWidth:1.5];
                [txt_Name.layer setBorderColor:[UIColor redColor].CGColor];
            }
            else
            {
                [txt_Name.layer setBorderWidth:1.5];
                [txt_Name.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
            }
            
            if([[[NSUserDefaults standardUserDefaults] valueForKey:@"smsRoute"]  isEqualToString:@""])
            {
                
                
            }
            else
            {
                if([txt_Phone_Or_Mail.text isEqualToString:@""])
                {
                    [txt_Phone_Or_Mail.layer setBorderWidth:1.5];
                    [txt_Phone_Or_Mail.layer setBorderColor:[UIColor redColor].CGColor];
                }
                else
                {
                    [txt_Phone_Or_Mail.layer setBorderWidth:1.5];
                    [txt_Phone_Or_Mail.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
                }
            }
            
            if([txt_your_party.text isEqualToString:@""])
            {
                [txt_your_party.layer setBorderWidth:1.5];
                [txt_your_party.layer setBorderColor:[UIColor redColor].CGColor];
            }
            else
            {
                [txt_your_party.layer setBorderWidth:1.5];
                [txt_your_party.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
            }
        }
    }
    else
    {
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:@"Please check your internet connection." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        
        [alert show];
    }
    
    //[self performSelector:@selector(handleLockButton) withObject:nil afterDelay:1];
}

-(NSString *)JSONString:(NSString *)aString
{
    NSMutableString *s = [NSMutableString stringWithString:aString];
    [s replaceOccurrencesOfString:@"\"" withString:@"\\\"" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [s length])];
    [s replaceOccurrencesOfString:@"/" withString:@"\\/" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [s length])];
    [s replaceOccurrencesOfString:@"\n" withString:@"\\n" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [s length])];
    [s replaceOccurrencesOfString:@"\b" withString:@"\\b" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [s length])];
    [s replaceOccurrencesOfString:@"\f" withString:@"\\f" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [s length])];
    [s replaceOccurrencesOfString:@"\r" withString:@"\\r" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [s length])];
    [s replaceOccurrencesOfString:@"\t" withString:@"\\t" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [s length])];
    return [NSString stringWithString:s];
}

-(NSString *) formatIdentificationNumber:(NSString *)string
{
    NSCharacterSet * invalidNumberSet = [NSCharacterSet characterSetWithCharactersInString:@"\n_!@#$%^&*()[]{}'\".,<>:;|\\/?+=\t~`- "];
    
    NSString  * result  = @"";
    NSScanner * scanner = [NSScanner scannerWithString:string];
    NSString  * scannerResult;
    
    [scanner setCharactersToBeSkipped:nil];
    
    while (![scanner isAtEnd])
    {
        if([scanner scanUpToCharactersFromSet:invalidNumberSet intoString:&scannerResult])
        {
            result = [result stringByAppendingString:scannerResult];
        }
        else
        {
            if(![scanner isAtEnd])
            {
                [scanner setScanLocation:[scanner scanLocation]+1];
            }
        }
    }
    
    return result;
}

-(void)handleLockButton
{
    NSLog(@"requesting guided access");
    UIAccessibilityRequestGuidedAccessSession(YES, ^(BOOL didSucceed) {
        if (didSucceed)
        {
            NSLog(@"entered guided access");
            //self.inGuidedSessionMode = YES;
            [[[UIAlertView alloc] initWithTitle:@"entered single access mode" message:nil delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
        }
        else {
            NSLog(@"failed to enter guided access");
            [[[UIAlertView alloc] initWithTitle:@"Unable to enter single access mode" message:nil delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
        }
    });
}



- (IBAction)Btn_Thank_you_ok_Clicked:(id)sender
{
    Thank_View.hidden = YES;
}

# pragma mark -
# pragma mark - Realtime Delegate

// For Remain methods, look at ortcClient.h file

- (void) onConnected:(OrtcClient*) ortc
{
    NSString *strChannel = [NSString stringWithFormat:@"RSNT_GUEST_DEV_%@",[[NSUserDefaults standardUserDefaults] valueForKey:@"OrgId"]];
    
    [_ortcClient subscribe:strChannel subscribeOnReconnected:YES onMessage:^(OrtcClient* ortc, NSString* channel, NSString* message)
    {
        NSLog(@"Received at %@: %@", channel, message);
        
        NSData *data = [message dataUsingEncoding:NSUTF8StringEncoding];
        id json = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
        
        if([[json valueForKey:@"OP"] isEqualToString:@"ADD"])
        {
            lbl_Count.text = [NSString stringWithFormat:@"(%@)",[json valueForKey:@"totalPartiesWaiting"]];
            Lbl_Now_serving_Count.text = [NSString stringWithFormat:@"#%@",[json valueForKey:@"NOW_SERVING_GUEST_ID"]];
            
            //Lbl_Est_Time.text = [NSString stringWithFormat:@"%@",[json valueForKey:@"totalWaitTime"]];
            
            NSString *minutes = [NSString stringWithFormat:@"%@",[json valueForKey:@"ORG_TOTAL_WAIT_TIME"]];
            int hour = [minutes intValue] / 60;
            int min = [minutes intValue] % 60;
            NSString *timeString = [NSString stringWithFormat:@"%.2d⁚%02d", hour, min];
            Lbl_Est_Time.text = [NSString stringWithFormat:@"%@",timeString];
            
            lblHours.text = [NSString stringWithFormat:@"%.2d",hour];
            lblMinutes.text = [NSString stringWithFormat:@"%02d",min];
            lblColon.hidden = false;
            
            Lbl_Number.text = [NSString stringWithFormat:@"#%@",[json valueForKey:@"guestRank"]];
            
        }
        else if([[json valueForKey:@"OP"] isEqualToString:@"DEL"])
        {
            lbl_Count.text = [NSString stringWithFormat:@"(%@)",[json valueForKey:@"numberofparties"]];
            Lbl_Now_serving_Count.text = [NSString stringWithFormat:@"#%@",[json valueForKey:@"NOW_SERVING_GUEST_ID"]];
            
           // Lbl_Est_Time.text = [NSString stringWithFormat:@"%@",[json valueForKey:@"totalWaitTime"]];
            NSString *minutes = [NSString stringWithFormat:@"%@",[json valueForKey:@"ORG_TOTAL_WAIT_TIME"]];
            int hour = [minutes intValue] / 60;
            int min = [minutes intValue] % 60;
            NSString *timeString = [NSString stringWithFormat:@"%.2d⁚%02d", hour, min];
            Lbl_Est_Time.text = [NSString stringWithFormat:@"%@",timeString];
            
            lblHours.text = [NSString stringWithFormat:@"%.2d",hour];
            lblMinutes.text = [NSString stringWithFormat:@"%02d",min];
            lblColon.hidden = false;
        }
        else if([[json valueForKey:@"OP"] isEqualToString:@"UpdageGuestInfo"])
        {
            /*lbl_Count.text = [NSString stringWithFormat:@"%@",[json valueForKey:@"nowServingParty"]];
            Lbl_Now_serving_Count.text = [NSString stringWithFormat:@"#%@",[json valueForKey:@"NOW_SERVING_GUEST_ID"]];
            
            //Lbl_Est_Time.text = [NSString stringWithFormat:@"%@",[json valueForKey:@"totalWaitTime"]];
            NSString *minutes = [NSString stringWithFormat:@"%@",[json valueForKey:@"totalWaitTime"]];
            int hour = [minutes intValue] / 60;
            int min = [minutes intValue] % 60;
            NSString *timeString = [NSString stringWithFormat:@"%.2d:%02d", hour, min];
            Lbl_Est_Time.text = [NSString stringWithFormat:@"%@",timeString];*/
        }
        else if([[json valueForKey:@"OP"] isEqualToString:@"UPD"])
        {
            lbl_Count.text = [NSString stringWithFormat:@"(%@)",[json valueForKey:@"ORG_GUEST_COUNT"]];
            Lbl_Now_serving_Count.text = [NSString stringWithFormat:@"#%@",[json valueForKey:@"NOW_SERVING_GUEST_ID"]];
            
            //Lbl_Est_Time.text = [NSString stringWithFormat:@"%@",[json valueForKey:@"totalWaitTime"]];
            NSString *minutes = [NSString stringWithFormat:@"%@",[json valueForKey:@"ORG_TOTAL_WAIT_TIME"]];
            int hour = [minutes intValue] / 60;
            int min = [minutes intValue] % 60;
            NSString *timeString = [NSString stringWithFormat:@"%.2d⁚%02d", hour, min];
            Lbl_Est_Time.text = [NSString stringWithFormat:@"%@",timeString];
            
            lblHours.text = [NSString stringWithFormat:@"%.2d",hour];
            lblMinutes.text = [NSString stringWithFormat:@"%02d",min];
            lblColon.hidden = false;
        }
        else if([[json valueForKey:@"OP"] isEqualToString:@"MARK_AS_SEATED"])
        {
            lbl_Count.text = [NSString stringWithFormat:@"(%@)",[json valueForKey:@"ORG_GUEST_COUNT"]];
            Lbl_Now_serving_Count.text = [NSString stringWithFormat:@"#%@",[json valueForKey:@"NOW_SERVING_GUEST_ID"]];
            
            //Lbl_Est_Time.text = [NSString stringWithFormat:@"%@",[json valueForKey:@"totalWaitTime"]];
            NSString *minutes = [NSString stringWithFormat:@"%@",[json valueForKey:@"ORG_TOTAL_WAIT_TIME"]];
            int hour = [minutes intValue] / 60;
            int min = [minutes intValue] % 60;
            NSString *timeString = [NSString stringWithFormat:@"%.2d⁚%02d", hour, min];
            Lbl_Est_Time.text = [NSString stringWithFormat:@"%@",timeString];
            
            lblHours.text = [NSString stringWithFormat:@"%.2d",hour];
            lblMinutes.text = [NSString stringWithFormat:@"%02d",min];
            lblColon.hidden = false;
        }
        
    }];
}


- (IBAction)btnLogout_clicked:(id)sender
{
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Kyobee" message:@"Are you sure you want to logout?" delegate:self cancelButtonTitle:@"NO" otherButtonTitles:@"YES", nil];
    
    alert.tag = 101;
    
    [alert show];
    
    
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    
    if (alertView.tag == 101)
    {
        if(buttonIndex == 1)
        {
            [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"rememberMe"];
            
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"checkinmodeselected"];
            
            [[NSUserDefaults standardUserDefaults]synchronize];
            
            [self.navigationController popToRootViewControllerAnimated:YES];
        }
    }
}

#pragma mark -
#pragma mark - Text Field Delegate Methods

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    txtFieldRef = textField;
    
    UIInterfaceOrientation orientation = [UIApplication sharedApplication].statusBarOrientation;
    
    if (UIInterfaceOrientationIsLandscape(orientation))
    {
        
        if(txtFieldRef == txt_Name)
        {
            _txtNameToTop.constant = -181;
            Lbl_Required.hidden = false;
            Btn_Close.hidden = false;
            
            [txt_Name reloadInputViews];
        }
        
        if(txtFieldRef == txt_Phone_Or_Mail)
        {
            _txtNameToTop.constant = -111;
            Lbl_Required.hidden = true;
            Btn_Close.hidden = true;
            
            if(btn_Cell_Phone.selected)
            {
                txt_Phone_Or_Mail.keyboardType = UIKeyboardTypePhonePad;
                txt_Phone_Or_Mail.autocorrectionType = UITextAutocorrectionTypeNo;
                
                [txt_Phone_Or_Mail reloadInputViews];
                
            }
        }
        
        if(txtFieldRef == txt_your_party)
        {
            _txtNameToTop.constant = -111;
            Lbl_Required.hidden = true;
            Btn_Close.hidden = true;
            
            return NO;
        }
    }
    
    [self.view setNeedsUpdateConstraints];
    
    [UIView animateWithDuration:0.40f animations:^{
        
        [self.view layoutIfNeeded];
    }];
    
    return YES;
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range
replacementString:(NSString *)string
{
    
    if(textField == txt_Phone_Or_Mail)
    {
        [txt_Phone_Or_Mail.layer setBorderWidth:1.5];
        [txt_Phone_Or_Mail.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
        
        if(btn_Cell_Phone.selected == true)
        {
            
            if([self checkforNumeric:string] == YES)
            {
                int length = (int)[self getLength:textField.text];
                //NSLog(@"Length  =  %d ",length);
                
                if(length == 10)
                {
                    if(range.length == 0)
                    {
                        [self hideKeyBoard];
                        return NO;
                        
                    }
                }
                
                if(length == 3)
                {
                    NSString *num = [self formatNumber:textField.text];
                    textField.text = [NSString stringWithFormat:@"(%@) ",num];
                    
                    if(range.length > 0)
                        textField.text = [NSString stringWithFormat:@"%@",[num substringToIndex:3]];
                }
                else if(length == 6)
                {
                    NSString *num = [self formatNumber:textField.text];
                    //NSLog(@"%@",[num  substringToIndex:3]);
                    //NSLog(@"%@",[num substringFromIndex:3]);
                    //textField.text = [NSString stringWithFormat:@"(%@) %@-",[num  substringToIndex:3],[num substringFromIndex:3]]; // Mayur
                    textField.text = [NSString stringWithFormat:@"(%@)-%@-",[num  substringToIndex:3],[num substringFromIndex:3]];
                    
                    if(range.length > 0)
                        textField.text = [NSString stringWithFormat:@"(%@)%@",[num substringToIndex:3],[num substringFromIndex:3]];
                }
                
                return YES;
                
                /*NSString *text = textField.text;
                
                NSString *acceptedcharacters = @"0123456789-/";
                NSCharacterSet *cs = [[NSCharacterSet characterSetWithCharactersInString:acceptedcharacters] invertedSet];
                const char * _char = [string cStringUsingEncoding:NSUTF8StringEncoding];
                int isBackSpace = strcmp(_char, "\b");
                if (isBackSpace == -8) {
                    NSLog(@"deleted");
                }
                else {
                    if (textField.text.length == 1) {
                        
                        textField.text = [NSString stringWithFormat:@"%@-",text];
                        return YES;
                    }
                    if (textField.text.length == 5) {
                        
                        textField.text = [NSString stringWithFormat:@"%@-",text];
                        return YES;
                    }
                    if (textField.text.length == 9) {
                        
                        textField.text = [NSString stringWithFormat:@"%@-",text];
                        return YES;
                    }
                }
                   if (textField == txt_Phone_Or_Mail) {
                NSUInteger newLength = [textField.text length] + [string length] - range.length;
                return (newLength > 14) ? NO : YES;
                
                  }
                
                NSString *filtered = [[string componentsSeparatedByCharactersInSet:cs] componentsJoinedByString:@""];
                
                return [string isEqualToString:filtered];*/
            }
            else
            {
                UIAlertView *objAlert = [[UIAlertView alloc] initWithTitle:@"Kyobee" message:@"please enter numbers only." delegate:nil cancelButtonTitle:nil otherButtonTitles:@"Close",nil];
                [objAlert show];
                
                
                return NO;
            }
        }
        else
        {
            return YES;
        }
    }
    else if (textField == txt_Name)
    {
        [txt_Name.layer setBorderWidth:1.5];
        [txt_Name.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
        
        
        
        return YES;
    }
    else
    {
        [txt_your_party.layer setBorderWidth:1.5];
        [txt_your_party.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
        
        return YES;
    }
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    if(textField == txt_Phone_Or_Mail)
    {
        if(btn_Email.selected == TRUE)
        {
            if([txt_Phone_Or_Mail.text length] > 0)
            {
                if(![self isEmailValid:txt_Phone_Or_Mail.text])
                {
                    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Kyobee" message:@"The email address that you typed is invalid. Please correct your email address and try again." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
                    [alert show];
                    txt_Phone_Or_Mail.text = @"";
                }
            }
        }
        else
        {
            NSString *number = [self formatIdentificationNumber:[NSString stringWithFormat:@"%@",txt_Phone_Or_Mail.text]];
            
            if([number length] > 0 &[number length] < 10)
            {
                
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Kyobee" message:@"The phone number that you typed is invalid. Please correct your phone number and try again." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
                [alert show];
                txt_Phone_Or_Mail.text = @"";
                
            }
        }
    }
    
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    NSInteger nextTag = textField.tag + 1;
    
    UIResponder *nextResponder = [textField.superview viewWithTag:nextTag];
    
    if (nextResponder)
    {
        Lbl_Required.hidden = false;
        Btn_Close.hidden = false;
        _txtNameToTop.constant = -181;
        
        CGSize screenSize = [[UIScreen mainScreen] bounds].size;
        
        if(screenSize.height == 480.0f)
        {
            
        }
        else
        {
            
        }
        
        [txtFieldRef becomeFirstResponder];
        
        [self.view setNeedsUpdateConstraints];
        
        [UIView animateWithDuration:0.40f animations:^{
            [self.view layoutIfNeeded];
        }];
        
        [nextResponder becomeFirstResponder];
    }
    else
    {
        Lbl_Required.hidden = false;
        Btn_Close.hidden = false;
        _txtNameToTop.constant = -181;
        
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
    
    
    
    CGSize screenSize = [[UIScreen mainScreen] bounds].size;
    
    if(screenSize.height == 480.0f)
    {
        
    }
    else
    {
        
    }
    
    Lbl_Required.hidden = false;
    Btn_Close.hidden = false;
    _txtNameToTop.constant = -181;
    
    
    tblViewParties.hidden = true;
    
    [txtFieldRef resignFirstResponder];
    
    [self.view setNeedsUpdateConstraints];
    
    [UIView animateWithDuration:0.40f animations:^{
        [self.view layoutIfNeeded];
    }];
}

-(BOOL) checkforNumeric:(NSString*) str
{
    if(str.length > 0)
    {
        NSCharacterSet *numbersOnly = [NSCharacterSet characterSetWithCharactersInString:@"0123456789"];
        NSCharacterSet *characterSetFromTextField = [NSCharacterSet characterSetWithCharactersInString:str];
        
        BOOL stringIsValid = [numbersOnly isSupersetOfSet:characterSetFromTextField];
        return stringIsValid;
    }
    return YES;
}


- (NSString *)formatNumber:(NSString *)mobileNumber
{
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"(" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@")" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@" " withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"-" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"+" withString:@""];
    
    NSLog(@"%@", mobileNumber);
    
    int length = (int)[mobileNumber length];
    if(length > 10)
    {
        mobileNumber = [mobileNumber substringFromIndex: length-10];
        NSLog(@"%@", mobileNumber);
        
    }
    
    return mobileNumber;
}

- (int)getLength:(NSString *)mobileNumber
{
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"(" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@")" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@" " withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"-" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"+" withString:@""];
    
    int length = (int)[mobileNumber length];
    
    return length;
}

- (void)hideKeyBoard
{
    for (UIView * txt in self.view.subviews){
        if ([txt isKindOfClass:[UITextField class]] && [txt isFirstResponder]) {
            [txt resignFirstResponder];
        }
    }
    
    
    CGSize screenSize = [[UIScreen mainScreen] bounds].size;
    
    if(screenSize.height == 480.0f)
    {
        
    }
    else
    {
        
    }
    
    Lbl_Required.hidden = false;
    Btn_Close.hidden = false;
    _txtNameToTop.constant = -181;
    
    [txtFieldRef resignFirstResponder];
    
    [self.view setNeedsUpdateConstraints];
    
    [UIView animateWithDuration:0.40f animations:^{
        [self.view layoutIfNeeded];
    }];
}

-(BOOL)isEmailValid:(NSString*)string
{
    NSString *emailRegex = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
    
    if([emailTest evaluateWithObject:string])
        return YES;
    else
        return NO;
}


- (void)loadTimeMatrics
{
        if(appDelegate.isInternetReachble)
        {
            
            [appDelegate startActivityIndicator:self.view];
            self.view.userInteractionEnabled = TRUE;
            
            
            //NSString *urlStr = [NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/Rsnt/seam/resource/restv1/loginAuthRestAction/loginCredAuth?username=jkim@kyobee.com&password=jaekim"];
            
            //NSString *urlStr = [NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/Rsnt/seam/resource/restv1/waitlistRestAction/totalwaittimemetricks?orgid=%@",[[NSUserDefaults standardUserDefaults] valueForKey:@"OrgId"]];
            
            NSString *urlStr = [NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/kyobee/web/rest/waitlistRestAction/totalwaittimemetricks?orgid=%@",[[NSUserDefaults standardUserDefaults] valueForKey:@"OrgId"]];
            
            
            NSURL *url = [NSURL URLWithString:urlStr];
            
            
            NSURLRequest *request = [NSURLRequest requestWithURL:url];
            
            
            
            
            /*NSString *urlStr;
            
            urlStr = [NSString stringWithFormat:@"orgid=%@",[[NSUserDefaults standardUserDefaults] valueForKey:@"OrgId"]];
            
            NSURL *aUrl = [NSURL URLWithString:[NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/kyobee/web/rest/waitlistRestAction/totalwaittimemetricks"]];
            //NSURL *aUrl = [NSURL URLWithString:@"http://dev-ronaktest.rhcloud.com/readyBApp/rest/profile/address"];
            //NSLog(@"Ping:- %@",postString);
            NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:aUrl cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:60.0];
            
            [request setHTTPMethod:@"POST"];
            
            NSData *parameterData = [urlStr dataUsingEncoding:NSUTF8StringEncoding allowLossyConversion:YES];
            NSString *postLength = [NSString stringWithFormat:@"%lu", (unsigned long)[parameterData length]];
            
            [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
            [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
            [request setHTTPBody:parameterData];*/
            
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
                     
                     //if([[json valueForKey:@"success"] isEqualToString:@"1"])
                     if([[json valueForKey:@"status"] isEqualToString:@"SUCCESS"])
                     {
                         lbl_Count.text = [NSString stringWithFormat:@"(%@)",[[json valueForKey:@"serviceResult"]valueForKey:@"ORG_GUEST_COUNT"]];
                         Lbl_Now_serving_Count.text = [NSString stringWithFormat:@"#%@",[[json valueForKey:@"serviceResult"]valueForKey:@"GUEST_RANK_MIN"]];
                         
                         //Lbl_Est_Time.text = [NSString stringWithFormat:@"%@",[json valueForKey:@"totalWaitTime"]];
                         
                         NSString *minutes = [NSString stringWithFormat:@"%@",[[json valueForKey:@"serviceResult"]valueForKey:@"ORG_TOTAL_WAIT_TIME"]];
                         int hour = [minutes intValue] / 60;
                         int min = [minutes intValue] % 60;
                         
                         //NSString *timeString = [NSString stringWithFormat:@"%.2d⁚%02d", hour, min];
                         NSString *timeString = [NSString stringWithFormat:@"%.2d⁚%02d", hour, min];
                         
                         Lbl_Est_Time.text = [NSString stringWithFormat:@"%@",timeString];
                         
                         lblHours.text = [NSString stringWithFormat:@"%.2d",hour];
                         lblMinutes.text = [NSString stringWithFormat:@"%02d",min];
                         lblColon.hidden = false;
                         
                         //NSString *timeString = [NSString stringWithFormat:@"%.2d⁚%02d", hour, min];
                         
                         
                         //[Img_User_Logo sd_setImageWithURL:[NSURL URLWithString:[[json valueForKey:@"serviceResult"]valueForKey:@"imageOrgPath"]] placeholderImage:[UIImage imageNamed:@"RestoImage"]];
                         
                         [Img_User_Logo sd_setImageWithURL:[NSURL URLWithString:[[json valueForKey:@"serviceResult"]valueForKey:@"imageOrgPath"]]
                                         placeholderImage:[UIImage imageNamed:@"RestoImage"]
                                                  options:SDWebImageRefreshCached];
                     }
                     else if([[json valueForKey:@"success"] isEqualToString:@"-1"])
                     {
                         /*UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:[json valueForKey:@"error"] delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                         
                         [alert show];*/
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

@end
