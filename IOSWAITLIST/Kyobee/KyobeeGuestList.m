//
//  KyobeeGuestList.m
//  Kyobee
//
//  Created by Mayur Pandya on 28/03/17.
//
//

#import "KyobeeGuestList.h"

#import "GuestListTwoColumn.h"
#import "GuestListOneColumn.h"

#import "GuestListCell.h"

#import "GuestListFourthColumn.h"

///*** web image
#import "UIImageView+WebCache.h"
//****

@interface KyobeeGuestList ()

@property (nonatomic,strong) UILongPressGestureRecognizer *lpgrLogOut;

@property (nonatomic,strong) UILongPressGestureRecognizer *lpgrForOptions;

@end

@implementation KyobeeGuestList

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    appDelegate =(AppDelegate*)[[UIApplication sharedApplication]delegate];
    
    guestArray = [[NSMutableArray alloc] initWithCapacity:0];
    
    arrayLess30 = [[NSMutableArray alloc] initWithCapacity:0];
    arrayLess60 = [[NSMutableArray alloc] initWithCapacity:0];
    arrayLess90 = [[NSMutableArray alloc] initWithCapacity:0];
    
    // Realtime
    _ortcClient = [OrtcClient ortcClientWithConfig:self];
    [_ortcClient setClusterUrl:@"https://ortc-developers.realtime.co/server/ssl/2.1"];
    [_ortcClient connect:@"j9MLMa" authenticationToken:@"testToken"];
    //

    self.lpgrLogOut = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(handleLongPressGestures:)];
    self.lpgrLogOut.minimumPressDuration = 2.0f;
    self.lpgrLogOut.numberOfTouchesRequired = 2;
    self.lpgrLogOut.allowableMovement = 100.0f;
    
    [btnLogoutFromLongPress addGestureRecognizer:self.lpgrLogOut];
    
    // For Options
    self.lpgrForOptions = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(handleLongPressGestures:)];
    self.lpgrForOptions.minimumPressDuration = 2.0f;
    self.lpgrForOptions.numberOfTouchesRequired = 2;
    self.lpgrForOptions.allowableMovement = 100.0f;
    
    [btnForOptions addGestureRecognizer:self.lpgrForOptions];
    
    columnsArray = [[NSMutableArray alloc] initWithCapacity:0];
    NSArray *colArray = [NSArray arrayWithObjects:@"1",@"2",@"3",@"4", nil];
    [columnsArray addObjectsFromArray:colArray];
    
    
    rowsArray = [[NSMutableArray alloc] initWithCapacity:0];
   /* NSArray *rwArray = [NSArray arrayWithObjects:@"25",@"30",@"35",@"40",@"45",@"50",@"55",@"60",@"65",@"70",@"75",@"80",@"85",@"90",@"95",@"100", nil];
    [rowsArray addObjectsFromArray:rwArray];*/
    
    tblView30.userInteractionEnabled = false;
    tblView60.userInteractionEnabled = false;
    tblView90.userInteractionEnabled = false;
}

- (void)handleLongPressGestures:(UILongPressGestureRecognizer *)sender
{
    if ([sender isEqual:self.lpgrLogOut])
    {
        if (sender.state == UIGestureRecognizerStateBegan)
        {
            [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"rememberMe"];
            
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"displaymodeselected"];
            
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"checkinmodeselected"];
            
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"fromBack"];
            
            
            //**
            
            [[NSUserDefaults standardUserDefaults] setValue:@"" forKey:@"numberOfColumns"];
            [[NSUserDefaults standardUserDefaults] setValue:@"" forKey:@"numberOfRows"];
            [[NSUserDefaults standardUserDefaults] setValue:@"" forKey:@"columnOneName"];
            [[NSUserDefaults standardUserDefaults] setValue:@"" forKey:@"columnTwoName"];
            [[NSUserDefaults standardUserDefaults] setValue:@"" forKey:@"columnThreeName"];
            
            //**
            
            [[NSUserDefaults standardUserDefaults]synchronize];
            
            [self.navigationController popToRootViewControllerAnimated:YES];
        }
    }
    
    if ([sender isEqual:self.lpgrForOptions])
    {
        if (sender.state == UIGestureRecognizerStateBegan)
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Options :" message:nil delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Go to back",@"Refresh",@"Settings", nil];
            
            /*UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Options :" message:nil delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Go to back",@"Refresh", nil];*/
            
            alert.tag = 101;
            
            [alert show];
        }
    }
    
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    
    [UIApplication sharedApplication].idleTimerDisabled = YES;
    
    [self fetchGuestList];
    
    //[self loadTimeMatrics];
    
    //NSLog(@"Logo FileName : %@", [[NSUserDefaults standardUserDefaults] valueForKey:@"logofile name"]);
    
    if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"logofile name"] isEqualToString:@""])
    {
        //http://jbossdev-kyobee.rhcloud.com/static/orglogos/2.png
        //[imgResto sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/static/orglogos/%@",[[NSUserDefaults standardUserDefaults] valueForKey:@"logofile name"]]] placeholderImage:[UIImage imageNamed:@"RestoImage"]];
        
        //NSLog(@"Logo FileName : %@", [NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/static/orglogos/%@",[[NSUserDefaults standardUserDefaults] valueForKey:@"logofile name"]]);
        
        [imgResto sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",logoUrl,[[NSUserDefaults standardUserDefaults] valueForKey:@"logofile name"]]] placeholderImage:[UIImage imageNamed:@"RestoImage"] options:SDWebImageRefreshCached];
    }
    
    UIInterfaceOrientation orientation = [UIApplication sharedApplication].statusBarOrientation;
    
    if (UIInterfaceOrientationIsLandscape(orientation))
    {
        lblCopyright.font = [UIFont systemFontOfSize:12.0];
    }
    else
    {
        lblCopyright.font = [UIFont systemFontOfSize:10.0];
    }
    
    [[NSNotificationCenter defaultCenter] addObserver:self  selector:@selector(orientationChanged:)    name:UIDeviceOrientationDidChangeNotification  object:nil];
    
    
    
    [txtColumns.layer setBorderWidth:1.5];
    [txtColumns.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
    
    [txtRows.layer setBorderWidth:1.5];
    [txtRows.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
    
    [txtColumnOne.layer setBorderWidth:1.5];
    [txtColumnOne.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
    
    [txtColumnTwo.layer setBorderWidth:1.5];
    [txtColumnTwo.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
    
    [txtColumnThree.layer setBorderWidth:1.5];
    [txtColumnThree.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
    
    [txtColumnFour.layer setBorderWidth:1.5];
    [txtColumnFour.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
    
    [viewSubSettings.layer setCornerRadius:3.0];
    
    [btnSettingsOK.layer setCornerRadius:3.0];
    
    [tblViewColumnsAndRows.layer setBorderWidth:1.5];
    [tblViewColumnsAndRows.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
    
    
    if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"columnOneName"] isEqualToString:@""])
    {
        lblColumnOneName.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnOneName"];
        
    }
    if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"columnTwoName"] isEqualToString:@""])
    {
        lblColumnTwoName.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnTwoName"];
        
    }
    if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"columnThreeName"] isEqualToString:@""])
    {
        lblColumnThreeName.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnThreeName"];
    }
    
    if([[NSUserDefaults standardUserDefaults] boolForKey:@"showParty"] == NO)
    {
        lblPartyOne.hidden = true;
        lblPartyTwo.hidden = true;
        lblPartyThree.hidden = true;
        
        _colOneGuestLead.constant = -25;
        _colTwoGuestLead.constant = -25;
        _colThreeGuestLead.constant = -25;
    }
    else
    {
        lblPartyOne.hidden = false;
        lblPartyTwo.hidden = false;
        lblPartyThree.hidden = false;
        
        _colOneGuestLead.constant = 25;
        _colTwoGuestLead.constant = 25;
        _colThreeGuestLead.constant = 25;
    }
    
    btnShowParty.clipsToBounds = YES;
    [btnShowParty.layer setBorderWidth:2.0];
    [btnShowParty.layer setBorderColor:[UIColor darkGrayColor].CGColor];
    [self setRoundedView:btnShowParty toDiameter:30.0];
    
    btnNotPresent.clipsToBounds = YES;
    [btnNotPresent.layer setBorderWidth:2.0];
    [btnNotPresent.layer setBorderColor:[UIColor colorWithRed:173.0/255.0 green:22.0/255.0 blue:45.0/255.0 alpha:1.0].CGColor];
    [self setRoundedView:btnNotPresent toDiameter:30.0];
    
    btnIncomplete.clipsToBounds = YES;
    [btnIncomplete.layer setBorderWidth:2.0];
    [btnIncomplete.layer setBorderColor:[UIColor colorWithRed:255.0/255.0 green:255.0/255.0 blue:102.0/255.0 alpha:1.0].CGColor];
    [self setRoundedView:btnIncomplete toDiameter:30.0];
}

-(void)setRoundedView:(UIButton *)roundedView toDiameter:(float)newSize;
{
    CGPoint saveCenter = roundedView.center;
    CGRect newFrame = CGRectMake(roundedView.frame.origin.x, roundedView.frame.origin.y, newSize, newSize);
    roundedView.frame = newFrame;
    roundedView.layer.cornerRadius = newSize / 2.0;
    roundedView.center = saveCenter;
}

- (void)orientationChanged:(NSNotification *)notification
{
    [self adjustViewsForOrientation:[[UIApplication sharedApplication] statusBarOrientation]];
}

- (void) adjustViewsForOrientation:(UIInterfaceOrientation) orientation
{
    
    [txtFieldRef resignFirstResponder];
    
    txtColumns.hidden = false;
    btnColumns.hidden = false;
    txtRows.hidden = false;
    btnRows.hidden = false;
    
    _txtColToTop.constant = 25;
    
    if (UIInterfaceOrientationIsLandscape(orientation))
    {
        lblCopyright.font = [UIFont systemFontOfSize:12.0];
    }
    else
    {
        lblCopyright.font = [UIFont systemFontOfSize:10.0];
    }

    [self.view setNeedsUpdateConstraints];
    
    [UIView animateWithDuration:0.40f animations:^{
        [self.view layoutIfNeeded];
    }];
}

-(void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    [[NSNotificationCenter defaultCenter]removeObserver:self name:UIDeviceOrientationDidChangeNotification object:nil];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)fetchGuestList
{
    if(appDelegate.isInternetReachble)
    {
        
        [appDelegate startActivityIndicator:self.view];
        self.view.userInteractionEnabled = TRUE;
        
        //{"filters":null,"sort":null,"sortOrder":null,"pageSize":500,"pageNo":1}
        
        //NSError *jsonError;
        NSData *objectData = [@"{\"filters\":null,\"sort\":null,\"sortOrder\":null,\"pageSize\":500,\"pageNo\":1}" dataUsingEncoding:NSUTF8StringEncoding];
        
        
        NSString *jsonString = [[NSString alloc] initWithData:objectData encoding:NSUTF8StringEncoding];
        
        /*NSData *data = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
         id json = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];*/
        
        
        //NSDictionary *json = [NSJSONSerialization JSONObjectWithData:objectData options:NSJSONReadingMutableContainers error:&jsonError];
        
        NSString *urlStr = [NSString stringWithFormat:@"%@web/rest/waitlistRestAction/checkinusers?orgid=%@&pagerReqParam=%@",ServiceUrl,[[NSUserDefaults standardUserDefaults] valueForKey:@"OrgId"],jsonString];
        
        NSString *escapedPath = [urlStr stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
        
        NSURL *url = [NSURL URLWithString:escapedPath];
        NSURLRequest *request = [NSURLRequest requestWithURL:url];
        
        [NSURLConnection sendAsynchronousRequest:request queue:[NSOperationQueue mainQueue] completionHandler:^(NSURLResponse *response, NSData *data, NSError *connectionError)
         {
             self.view.userInteractionEnabled = TRUE;
             [appDelegate stopActivityIndicator];
             
             if(data != nil)
             {
                 NSString *htmlSTR = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
                 //NSLog(@"%@",htmlSTR);
                 
                 NSData *data = [htmlSTR dataUsingEncoding:NSUTF8StringEncoding];
                 id json = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
                 
                 //NSDictionary *jsonObject=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:nil];
                 
                 //if([[json valueForKey:@"success"] isEqualToString:@"1"])
                 if(![[json valueForKey:@"status"] isEqual:[NSNull null]])
                 {
                     if([[json valueForKey:@"status"] isEqualToString:@"SUCCESS"])
                     {
                         if(![[json valueForKey:@"serviceResult"] isEqual:[NSNull null]])
                         {
                             NSArray *guestList = [[json valueForKey:@"serviceResult"] valueForKey:@"records"];
                             
                             if(guestList.count > 0)
                             {
                                 if(guestArray.count > 0)
                                 {
                                     [guestArray removeAllObjects];
                                 }
                                 
                                 [guestArray addObjectsFromArray:guestList];
                                 
                                 
                                 
                                 if(arrayLess30.count > 0)
                                 {
                                     [arrayLess30 removeAllObjects];
                                 }
                                 if(arrayLess60.count > 0)
                                 {
                                     [arrayLess60 removeAllObjects];
                                 }
                                 if(arrayLess90.count > 0)
                                 {
                                     [arrayLess90 removeAllObjects];
                                 }
                                 
                                 // For Less Than 30
                                 /*for(int i = 0; i < [guestArray count]; i++)
                                 {
                                     if([[[guestArray objectAtIndex:i] valueForKey:@"WaitTime"] integerValue] <= 30)
                                     {
                                         [arrayLess30 addObject:[guestArray objectAtIndex:i]];
                                     }
                                 }*/
                                 
                                 
                                 if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfRows"] isEqualToString:@""])
                                 {
                                     int numberOfRows = [[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfRows"] intValue];
                                     
                                     for(int i = 0; i < [guestArray count]; i++)
                                     {
                                         
                                         [arrayLess30 addObject:[guestArray objectAtIndex:i]];
                                         
                                         
                                         if([arrayLess30 count] == numberOfRows)
                                         {
                                             break;
                                         }
                                     }
                                 }
                                 else
                                 {
                                     for(int i = 0; i < [guestArray count]; i++)
                                     {
                                         
                                         [arrayLess30 addObject:[guestArray objectAtIndex:i]];
                                         
                                         if(i == 24)
                                         {
                                             break;
                                         }
                                     }
                                 }
                                 
                                 /*for(int i = 0; i < [guestArray count]; i++)
                                 {
                                     
                                     [arrayLess30 addObject:[guestArray objectAtIndex:i]];
                                     
                                     if(i == 24)
                                     {
                                         break;
                                     }
                                 }*/
                                 
                                 if(arrayLess30.count > 0)
                                 {
                                     [tblView30 reloadData];
                                     
                                     lblNoUser30.hidden = true;
                                 }
                                 else
                                 {
                                     lblNoUser30.hidden = true;
                                 }
                                 //
                                 
                                 
                                 
                                 // For Less Than 60
                                 /*for(int i = 0; i < [guestArray count]; i++)
                                 {
                                     if([[[guestArray objectAtIndex:i] valueForKey:@"WaitTime"] integerValue] > 30 & [[[guestArray objectAtIndex:i] valueForKey:@"WaitTime"] integerValue] <= 60)
                                     //if([[[guestArray objectAtIndex:i] valueForKey:@"WaitTime"] integerValue] <= 60)
                                     {
                                         [arrayLess60 addObject:[guestArray objectAtIndex:i]];
                                     }
                                 }*/
                                 
                                 if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfRows"] isEqualToString:@""])
                                 {
                                     int numberOfRows = [[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfRows"] intValue];
                                     
                                     int rows = (int)[arrayLess30 count];
                                     
                                     for(int i = rows; i < [guestArray count]; i++)
                                     {
                                         
                                         [arrayLess60 addObject:[guestArray objectAtIndex:i]];
                                         
                                         if([arrayLess60 count] == numberOfRows)
                                         {
                                             break;
                                         }
                                         
                                     }
                                 }
                                 else
                                 {
                                     for(int i = 25; i < [guestArray count]; i++)
                                     {
                                         
                                         [arrayLess60 addObject:[guestArray objectAtIndex:i]];
                                         
                                         if(i == 49)
                                         {
                                             break;
                                         }
                                         
                                     }
                                 }
                                 
                                 /*for(int i = 25; i < [guestArray count]; i++)
                                 {
                                     
                                    [arrayLess60 addObject:[guestArray objectAtIndex:i]];
                                     
                                     if(i == 49)
                                     {
                                         break;
                                     }
                                     
                                 }*/
                                 
                                 if(arrayLess60.count > 0)
                                 {
                                     [tblView60 reloadData];
                                     
                                     lblNoUser60.hidden = true;
                                 }
                                 else
                                 {
                                     lblNoUser60.hidden = true;
                                 }
                                 //
                                 
                                 
                                 // For Less Than 90
                                 /*for(int i = 0; i < [guestArray count]; i++)
                                 {
                                     if([[[guestArray objectAtIndex:i] valueForKey:@"WaitTime"] integerValue] > 60 & [[[guestArray objectAtIndex:i] valueForKey:@"WaitTime"] integerValue] <= 90)
                                     //if([[[guestArray objectAtIndex:i] valueForKey:@"WaitTime"] integerValue] <= 90)
                                     {
                                         [arrayLess90 addObject:[guestArray objectAtIndex:i]];
                                     }
                                 }*/
                                 
                                 
                                 if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfRows"] isEqualToString:@""])
                                 {
                                     int numberOfRows = [[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfRows"] intValue];
                                     
                                     int rows1 = (int)[arrayLess30 count];
                                     int rows2 = (int)[arrayLess60 count];
                                     
                                     int row = rows1 + rows2;
                                     
                                     for(int i = row; i < [guestArray count]; i++)
                                     {
                                         [arrayLess90 addObject:[guestArray objectAtIndex:i]];
                                         
                                         if([arrayLess90 count] == numberOfRows)
                                         {
                                             break;
                                         }
                                         
                                     }
                                 }
                                 else
                                 {
                                     for(int i = 50; i < [guestArray count]; i++)
                                     {
                                         [arrayLess90 addObject:[guestArray objectAtIndex:i]];
                                         
                                         if(i == 74)
                                         {
                                             break;
                                         }
                                         
                                     }
                                 }
                                 
                                 /*for(int i = 50; i < [guestArray count]; i++)
                                 {
                                    [arrayLess90 addObject:[guestArray objectAtIndex:i]];
                                     
                                     if(i == 74)
                                     {
                                         break;
                                     }
                                     
                                 }*/
                                 
                                 if(arrayLess90.count > 0)
                                 {
                                     [tblView90 reloadData];
                                     
                                     lblNoUser90.hidden = true;
                                 }
                                 else
                                 {
                                     lblNoUser90.hidden = true;
                                 }
                                 //
                                 
                                 [tblView30 reloadData];
                                 [tblView60 reloadData];
                                 [tblView90 reloadData];
                                 
                             }
                             else
                             {
                                 UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:@"There is no guest checked in yet." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                                 
                                 [alert show];
                             }
                         }
                         else
                         {
                             UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:@"There is no guest checked in yet." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                             
                             [alert show];
                         }
                         
                         //[imgResto sd_setImageWithURL:[NSURL URLWithString:[[json valueForKey:@"serviceResult"]valueForKey:@"imageOrgPath"]] placeholderImage:[UIImage imageNamed:@"RestoImage"]];
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
                 else
                 {
                     if(arrayLess30.count > 0)
                     {
                         [arrayLess30 removeAllObjects];
                     }
                     if(arrayLess60.count > 0)
                     {
                         [arrayLess60 removeAllObjects];
                     }
                     if(arrayLess90.count > 0)
                     {
                         [arrayLess90 removeAllObjects];
                     }
                     
                     [tblView30 reloadData];
                     [tblView60 reloadData];
                     [tblView90 reloadData];
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

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

#pragma mark - UITableViewDataSource


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(viewSettings.hidden == true)
    {
        
        if(tableView == tblView30)
        {
            return arrayLess30.count;
        }
        else if (tableView == tblView60)
        {
            return arrayLess60.count;
        }
        else
        {
            return arrayLess90.count;
        }
    }
    else
    {
        if(btnColumns.selected == true)
        {
            return columnsArray.count + 1;
        }
        else
        {
            return rowsArray.count + 1;
        }
    }
    
    //return guestArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 25;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(viewSettings.hidden == true)
    {
        
        if(tableView == tblView30)
        {
            static NSString *CellIdentifier = @"Cell";
            
            GuestListCell *cell = (GuestListCell *)[tblView30 dequeueReusableCellWithIdentifier:CellIdentifier];
            
            if (cell == nil)
            {
                cell = [[[NSBundle mainBundle]loadNibNamed:NSStringFromClass([GuestListCell class]) owner:nil options:nil] lastObject];
            }
            
            cell.lblRank.text = [NSString stringWithFormat:@"%@",[[arrayLess30 objectAtIndex:indexPath.row] valueForKey:@"rank"]];
            cell.lblName.text = [NSString stringWithFormat:@"%@",[[arrayLess30 objectAtIndex:indexPath.row] valueForKey:@"name"]];
            
            /*if (![[[arrayLess30 objectAtIndex:indexPath.row]valueForKey:@"calloutCount"] isEqual:[NSNull null]])
            {
                cell.imgThumb.hidden = false;
            }
            
            if (![[[arrayLess30 objectAtIndex:indexPath.row]valueForKey:@"incompleteParty"] isEqual:[NSNull null]])
            {
                cell.backgroundColor = [UIColor colorWithRed:255.0/255.0 green:255.0/255.0 blue:102.0/255.0 alpha:1.0];
            }
            else
            {
                cell.backgroundColor = [UIColor clearColor];
            }*/
            
            if([[NSUserDefaults standardUserDefaults] boolForKey:@"showParty"] == NO)
            {
                cell.lblRank.hidden = true;
                cell.lblNameLead.constant = -25;
            }
            else
            {
                cell.lblRank.hidden = false;
                cell.lblNameLead.constant = 25;
            }
            
            if([[NSUserDefaults standardUserDefaults] boolForKey:@"showNotPresent"] == YES)
            {
                if (![[[arrayLess30 objectAtIndex:indexPath.row]valueForKey:@"calloutCount"] isEqual:[NSNull null]])
                {
                    cell.imgThumb.hidden = false;
                }
                else
                {
                    cell.imgThumb.hidden = true;
                }
            }
            else
            {
                cell.imgThumb.hidden = true;
            }
            
            if([[NSUserDefaults standardUserDefaults] boolForKey:@"showIncomplete"] == YES)
            {
                if (![[[arrayLess30 objectAtIndex:indexPath.row]valueForKey:@"incompleteParty"] isEqual:[NSNull null]])
                {
                    cell.backgroundColor = [UIColor colorWithRed:255.0/255.0 green:255.0/255.0 blue:102.0/255.0 alpha:1.0];
                    
                    cell.contentView.backgroundColor = [UIColor colorWithRed:255.0/255.0 green:255.0/255.0 blue:102.0/255.0 alpha:1.0];
                }
                else
                {
                    cell.backgroundColor = [UIColor clearColor];
                    
                    cell.contentView.backgroundColor = [UIColor clearColor];
                }
            }
            else
            {
                cell.backgroundColor = [UIColor clearColor];
                
                cell.contentView.backgroundColor = [UIColor clearColor];
            }
            
            
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            return cell;
        }
        else if(tableView == tblView60)
        {
            static NSString *CellIdentifier = @"Cell";
            
            GuestListCell *cell = (GuestListCell *)[tblView30 dequeueReusableCellWithIdentifier:CellIdentifier];
            
            if (cell == nil)
            {
                cell = [[[NSBundle mainBundle]loadNibNamed:NSStringFromClass([GuestListCell class]) owner:nil options:nil] lastObject];
            }
            
            cell.lblRank.text = [NSString stringWithFormat:@"%@",[[arrayLess60 objectAtIndex:indexPath.row] valueForKey:@"rank"]];
            cell.lblName.text = [NSString stringWithFormat:@"%@",[[arrayLess60 objectAtIndex:indexPath.row] valueForKey:@"name"]];
            
            
            
            /*if (![[[arrayLess60 objectAtIndex:indexPath.row]valueForKey:@"calloutCount"] isEqual:[NSNull null]])
            {
                cell.imgThumb.hidden = false;
            }
            
            if (![[[arrayLess60 objectAtIndex:indexPath.row]valueForKey:@"incompleteParty"] isEqual:[NSNull null]])
            {
                cell.backgroundColor = [UIColor colorWithRed:255.0/255.0 green:255.0/255.0 blue:102.0/255.0 alpha:1.0];
            }
            else
            {
                cell.backgroundColor = [UIColor clearColor];
            }*/
            
            
            if([[NSUserDefaults standardUserDefaults] boolForKey:@"showParty"] == NO)
            {
                cell.lblRank.hidden = true;
                cell.lblNameLead.constant = -25;
            }
            else
            {
                cell.lblRank.hidden = false;
                cell.lblNameLead.constant = 25;
            }
            
            
            if([[NSUserDefaults standardUserDefaults] boolForKey:@"showNotPresent"] == true)
            {
                if (![[[arrayLess60 objectAtIndex:indexPath.row]valueForKey:@"calloutCount"] isEqual:[NSNull null]])
                {
                    cell.imgThumb.hidden = false;
                }
            }
            
            if([[NSUserDefaults standardUserDefaults] boolForKey:@"showIncomplete"] == true)
            {
                if (![[[arrayLess60 objectAtIndex:indexPath.row]valueForKey:@"incompleteParty"] isEqual:[NSNull null]])
                {
                    cell.backgroundColor = [UIColor colorWithRed:255.0/255.0 green:255.0/255.0 blue:102.0/255.0 alpha:1.0];
                    
                    cell.contentView.backgroundColor = [UIColor colorWithRed:255.0/255.0 green:255.0/255.0 blue:102.0/255.0 alpha:1.0];
                }
                else
                {
                    cell.backgroundColor = [UIColor clearColor];
                    
                    cell.contentView.backgroundColor = [UIColor clearColor];
                }
            }
            else
            {
                cell.backgroundColor = [UIColor clearColor];
                
                cell.contentView.backgroundColor = [UIColor clearColor];
            }
            
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            return cell;
        }
        else
        {
            static NSString *CellIdentifier = @"Cell";
            
            GuestListCell *cell = (GuestListCell *)[tblView90 dequeueReusableCellWithIdentifier:CellIdentifier];
            
            if (cell == nil)
            {
                cell = [[[NSBundle mainBundle]loadNibNamed:NSStringFromClass([GuestListCell class]) owner:nil options:nil] lastObject];
            }
            
            cell.lblRank.text = [NSString stringWithFormat:@"%@",[[arrayLess90 objectAtIndex:indexPath.row] valueForKey:@"rank"]];
            cell.lblName.text = [NSString stringWithFormat:@"%@",[[arrayLess90 objectAtIndex:indexPath.row] valueForKey:@"name"]];
            
            
            
            
            /*if (![[[arrayLess90 objectAtIndex:indexPath.row]valueForKey:@"calloutCount"] isEqual:[NSNull null]])
            {
                cell.imgThumb.hidden = false;
            }
            
            if (![[[arrayLess90 objectAtIndex:indexPath.row]valueForKey:@"incompleteParty"] isEqual:[NSNull null]])
            {
                cell.backgroundColor = [UIColor colorWithRed:255.0/255.0 green:255.0/255.0 blue:0.0/255.0 alpha:1.0];
            }
            else
            {
                cell.backgroundColor = [UIColor clearColor];
            }*/
            
            
            if([[NSUserDefaults standardUserDefaults] boolForKey:@"showParty"] == NO)
            {
                cell.lblRank.hidden = true;
                cell.lblNameLead.constant = -25;
            }
            else
            {
                cell.lblRank.hidden = false;
                cell.lblNameLead.constant = 25;
            }
            
            
            if([[NSUserDefaults standardUserDefaults] boolForKey:@"showNotPresent"] == true)
            {
                if (![[[arrayLess90 objectAtIndex:indexPath.row]valueForKey:@"calloutCount"] isEqual:[NSNull null]])
                {
                    cell.imgThumb.hidden = false;
                }
            }
            
            if([[NSUserDefaults standardUserDefaults] boolForKey:@"showIncomplete"] == true)
            {
                if (![[[arrayLess90 objectAtIndex:indexPath.row]valueForKey:@"incompleteParty"] isEqual:[NSNull null]])
                {
                    cell.backgroundColor = [UIColor colorWithRed:255.0/255.0 green:255.0/255.0 blue:102.0/255.0 alpha:1.0];
                    
                    cell.contentView.backgroundColor = [UIColor colorWithRed:255.0/255.0 green:255.0/255.0 blue:102.0/255.0 alpha:1.0];
                }
                else
                {
                    cell.backgroundColor = [UIColor clearColor];
                    
                    cell.contentView.backgroundColor = [UIColor clearColor];
                }
            }
            else
            {
                cell.backgroundColor = [UIColor clearColor];
                
                cell.contentView.backgroundColor = [UIColor clearColor];
            }
            
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            return cell;
        }
    }
    else
    {
        if(btnColumns.selected == true)
        {
            static NSString *cellIdentifier = @"Cell";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
            if (!cell)
            {
                cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
            }
            
            if(indexPath.row == 0)
            {
                cell.textLabel.text =[NSString stringWithFormat:@"# of columns*"];
            }
            else
            {
                cell.textLabel.text =[NSString stringWithFormat:@"%@",[columnsArray objectAtIndex:indexPath.row-1]];
            }
            
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.textLabel.textColor = [UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0];
            cell.textLabel.font = [UIFont boldSystemFontOfSize:20.0];
            
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
                cell.textLabel.text =[NSString stringWithFormat:@"# of rows per column*"];
            }
            else
            {
                cell.textLabel.text =[NSString stringWithFormat:@"%@",[rowsArray objectAtIndex:indexPath.row-1]] ;
            }
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.textLabel.textColor = [UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0];
            cell.textLabel.font = [UIFont boldSystemFontOfSize:20.0];
            
            return cell;
        }
    }
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(tableView == tblViewColumnsAndRows)
    {
    
        if(indexPath.row > 0)
        {
            if(btnColumns.selected == true)
            {
                txtColumns.text = [NSString stringWithFormat:@"%@",[columnsArray objectAtIndex:indexPath.row-1]];
                [tblViewColumnsAndRows setHidden:true];
                
                if(indexPath.row == 1)
                {
                    _heightViewSubSettings.constant = 498;
                    
                    NSArray *rwArray = [NSArray arrayWithObjects:@"25",@"30",@"35",@"40",@"45",@"50",@"55",@"60",@"65",@"70",@"75",@"80",@"85",@"90",@"95",@"100", nil];
                    
                    if(rowsArray.count > 0)
                        [rowsArray removeAllObjects];
                    
                    [rowsArray addObjectsFromArray:rwArray];
                    
                    txtRows.text = @"";
                    
                    txtColumnOne.hidden = false;
                    txtColumnTwo.hidden = true;
                    txtColumnThree.hidden = true;
                    txtColumnFour.hidden = true;
                    
                    txtColumnOne.placeholder = @"Enter first column name*";
                    
                    
                    [self.view setNeedsUpdateConstraints];
                    
                    [UIView animateWithDuration:0.40f animations:^{
                        
                        [self.view layoutIfNeeded];
                    }];
                }
                
                if(indexPath.row == 2)
                {
                    _heightViewSubSettings.constant = 578;
                    
                    NSArray *rwArray = [NSArray arrayWithObjects:@"25", nil];
                    
                    if(rowsArray.count > 0)
                        [rowsArray removeAllObjects];
                    
                    [rowsArray addObjectsFromArray:rwArray];
                    
                    txtRows.text = @"";
                    
                    txtColumnOne.hidden = false;
                    txtColumnTwo.hidden = false;
                    txtColumnThree.hidden = true;
                    txtColumnFour.hidden = true;
                    
                    txtColumnOne.placeholder = @"Enter first column name*";
                    txtColumnTwo.placeholder = @"Enter second column name*";
                    
                    
                    [self.view setNeedsUpdateConstraints];
                    
                    [UIView animateWithDuration:0.40f animations:^{
                        
                        [self.view layoutIfNeeded];
                    }];
                }
                
                if(indexPath.row == 3)
                {
                    _heightViewSubSettings.constant = 658;
                    
                    NSArray *rwArray = [NSArray arrayWithObjects:@"25", nil];
                    
                    if(rowsArray.count > 0)
                        [rowsArray removeAllObjects];
                    
                    [rowsArray addObjectsFromArray:rwArray];
                    
                    txtRows.text = @"";
                    
                    txtColumnOne.hidden = false;
                    txtColumnTwo.hidden = false;
                    txtColumnThree.hidden = false;
                    txtColumnFour.hidden = true;
                    
                    txtColumnOne.placeholder = @"Enter first column name*";
                    txtColumnTwo.placeholder = @"Enter second column name*";
                    txtColumnThree.placeholder = @"Enter third column name*";
                    
                    [self.view setNeedsUpdateConstraints];
                    
                    [UIView animateWithDuration:0.40f animations:^{
                        
                        [self.view layoutIfNeeded];
                    }];
                }
                
                if(indexPath.row == 4)
                {
                    _heightViewSubSettings.constant = 700;
                    
                    NSArray *rwArray = [NSArray arrayWithObjects:@"25", nil];
                    
                    if(rowsArray.count > 0)
                        [rowsArray removeAllObjects];
                    
                    [rowsArray addObjectsFromArray:rwArray];
                    
                    txtRows.text = @"";
                    
                    txtColumnOne.hidden = false;
                    txtColumnTwo.hidden = false;
                    txtColumnThree.hidden = false;
                    txtColumnFour.hidden = false;
                    
                    txtColumnOne.placeholder = @"Enter first column name*";
                    txtColumnTwo.placeholder = @"Enter second column name*";
                    txtColumnThree.placeholder = @"Enter third column name*";
                    txtColumnFour.placeholder = @"Enter fourth column name*";
                    
                    [self.view setNeedsUpdateConstraints];
                    
                    [UIView animateWithDuration:0.40f animations:^{
                        
                        [self.view layoutIfNeeded];
                    }];
                }
                
                [txtColumns.layer setBorderWidth:1.5];
                [txtColumns.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
            }
            else
            {
                txtRows.text = [NSString stringWithFormat:@"%@",[rowsArray objectAtIndex:indexPath.row-1]];
                [tblViewColumnsAndRows setHidden:true];
                
                
                [txtRows.layer setBorderWidth:1.5];
                [txtRows.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
            }
            
            /*txt_your_party.text = [NSString stringWithFormat:@"%ld",indexPath.row];
            [tblViewParties setHidden:true];
            [tblViewSeatPref reloadData];
            
            [txt_your_party.layer setBorderWidth:1.5];
            [txt_your_party.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];*/
        }
    }
}

- (IBAction)btnRefresh_clicked:(id)sender
{
    [self fetchGuestList];
}




# pragma mark -
# pragma mark - Realtime Delegate

// For Remain methods, look at ortcClient.h file

- (void) onConnected:(OrtcClient*) ortc
{
    NSString *strChannel = [NSString stringWithFormat:@"%@%@",ChannelName,[[NSUserDefaults standardUserDefaults] valueForKey:@"OrgId"]];
    
    [_ortcClient subscribe:strChannel subscribeOnReconnected:YES onMessage:^(OrtcClient* ortc, NSString* channel, NSString* message)
     {
         NSLog(@"Received at %@: %@", channel, message);
         
         NSData *data = [message dataUsingEncoding:NSUTF8StringEncoding];
         id json = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
         
         id jsonForNotPresentAndIcomplete;
         
         if([[json valueForKey:@"OP"] isEqualToString:@"ADD"])
         {
             
             [self fetchList];
             //Lbl_Number.text = [NSString stringWithFormat:@"#%@",[json valueForKey:@"guestRank"]];
             
         }
         else if([[json valueForKey:@"OP"] isEqualToString:@"DEL"])
         {
             [self fetchList];
             
             /*for(int i = 0; i<[guestArray count]; i++)
              {
              if([[[guestArray objectAtIndex:i] valueForKey:@"guestID"] isEqualToNumber:[json valueForKey:@"guestObj"]])
              {
              NSLog(@"GuestId : %@", [[guestArray objectAtIndex:i] valueForKey:@"guestID"]);
              NSLog(@"GuestId : %@", [json valueForKey:@"guestObj"]);
              
              [guestArray removeObjectAtIndex:i];
              
              [tblViewGuestList reloadData];
              }
              }*/
         }
         else if([[json valueForKey:@"OP"] isEqualToString:@"UpdageGuestInfo"])
         {
             
             [self fetchList];
             
             /*
             
             // from less 30
             for(int i = 0; i<[arrayLess30 count]; i++)
             {
                 if([[[arrayLess30 objectAtIndex:i] valueForKey:@"guestID"] isEqualToNumber:[json valueForKey:@"guestObj"]])
                 {
                     //NSLog(@"GuestId : %@", [[guestArray objectAtIndex:i] valueForKey:@"guestID"]);
                     //NSLog(@"GuestId : %@", [json valueForKey:@"guestObj"]);
                     
                     //NSLog(@"Name : %@", [[json valueForKey:@"updguest"] valueForKey:@"name"]);
                     
                     
                     NSDictionary *aDictionary = [arrayLess30 objectAtIndex: i];
                     //Make a mutable copy of each dictionary in the array.
                     NSMutableDictionary *mDict = [aDictionary mutableCopy];
                     
                     //Replace the value at key @"key" with some new value @"new value"
                     mDict[@"name"] = [[json valueForKey:@"updguest"] valueForKey:@"name"]; //Replace this part as needed
                     mDict[@"status"] = [[json valueForKey:@"updguest"] valueForKey:@"status"]; //Replace this part as needed

                     [arrayLess30 replaceObjectAtIndex:i withObject:mDict];
                     
                     [tblView30 reloadData];
                 }
             }
             
             // from less 60
             for(int i = 0; i<[arrayLess60 count]; i++)
             {
                 if([[[arrayLess60 objectAtIndex:i] valueForKey:@"guestID"] isEqualToNumber:[json valueForKey:@"guestObj"]])
                 {
                     //NSLog(@"GuestId : %@", [[guestArray objectAtIndex:i] valueForKey:@"guestID"]);
                     //NSLog(@"GuestId : %@", [json valueForKey:@"guestObj"]);
                     
                     //NSLog(@"Name : %@", [[json valueForKey:@"updguest"] valueForKey:@"name"]);
                     
                     
                     NSDictionary *aDictionary = [arrayLess60 objectAtIndex: i];
                     //Make a mutable copy of each dictionary in the array.
                     NSMutableDictionary *mDict = [aDictionary mutableCopy];
                     
                     //Replace the value at key @"key" with some new value @"new value"
                     mDict[@"name"] = [[json valueForKey:@"updguest"] valueForKey:@"name"]; //Replace this part as needed
                     mDict[@"status"] = [[json valueForKey:@"updguest"] valueForKey:@"status"]; //Replace this part as needed
                     
                     [arrayLess60 replaceObjectAtIndex:i withObject:mDict];
                     
                     [tblView60 reloadData];
                 }
             }
             
             
             // from less 90
             for(int i = 0; i<[arrayLess90 count]; i++)
             {
                 if([[[arrayLess90 objectAtIndex:i] valueForKey:@"guestID"] isEqualToNumber:[json valueForKey:@"guestObj"]])
                 {
                     //NSLog(@"GuestId : %@", [[guestArray objectAtIndex:i] valueForKey:@"guestID"]);
                     //NSLog(@"GuestId : %@", [json valueForKey:@"guestObj"]);
                     
                     //NSLog(@"Name : %@", [[json valueForKey:@"updguest"] valueForKey:@"name"]);
                     
                     
                     NSDictionary *aDictionary = [arrayLess90 objectAtIndex: i];
                     //Make a mutable copy of each dictionary in the array.
                     NSMutableDictionary *mDict = [aDictionary mutableCopy];
                     
                     //Replace the value at key @"key" with some new value @"new value"
                     mDict[@"name"] = [[json valueForKey:@"updguest"] valueForKey:@"name"]; //Replace this part as needed
                     mDict[@"status"] = [[json valueForKey:@"updguest"] valueForKey:@"status"]; //Replace this part as needed
                     
                     [arrayLess90 replaceObjectAtIndex:i withObject:mDict];
                     
                     [tblView90 reloadData];
                 }
             }
             
             */
         }
         else if([[json valueForKey:@"OP"] isEqualToString:@"UPD"])
         {
             [self fetchList];
             
             /*
             jsonForNotPresentAndIcomplete = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
             
             // from less 30
             for(int i = 0; i<[arrayLess30 count]; i++)
             {
                 if([[[arrayLess30 objectAtIndex:i] valueForKey:@"guestID"] isEqualToNumber:[json valueForKey:@"guestObj"]])
                 {
                     //NSLog(@"GuestId : %@", [[guestArray objectAtIndex:i] valueForKey:@"guestID"]);
                     //NSLog(@"GuestId : %@", [json valueForKey:@"guestObj"]);
                     
                     //NSLog(@"Name : %@", [[json valueForKey:@"updguest"] valueForKey:@"name"]);
                     
                     
                     NSDictionary *aDictionary = [arrayLess30 objectAtIndex: i];
                     //Make a mutable copy of each dictionary in the array.
                     NSMutableDictionary *mDict = [aDictionary mutableCopy];
                     
                     //Replace the value at key @"key" with some new value @"new value"
                     //mDict[@"name"] = [[json valueForKey:@"updguest"] valueForKey:@"name"]; //Replace this part as needed
                     mDict[@"status"] = [[json valueForKey:@"updguest"] valueForKey:@"status"]; //Replace this part as needed
                     
                     if(![[[json valueForKey:@"updguest"] valueForKey:@"calloutCount"] isEqual:[NSNull null]])
                     {
                         mDict[@"calloutCount"] = [[json valueForKey:@"updguest"] valueForKey:@"calloutCount"];
                     }
                     
                     if(![[[json valueForKey:@"updguest"] valueForKey:@"incompleteParty"] isEqual:[NSNull null]])
                     {
                         mDict[@"incompleteParty"] = [[json valueForKey:@"updguest"] valueForKey:@"incompleteParty"];
                     }
                     
                     [arrayLess30 replaceObjectAtIndex:i withObject:mDict];
                     
                     [tblView30 reloadData];
                     
                     
                 }
             }
             
             // from less 60
             for(int i = 0; i<[arrayLess60 count]; i++)
             {
                 if([[[arrayLess60 objectAtIndex:i] valueForKey:@"guestID"] isEqualToNumber:[json valueForKey:@"guestObj"]])
                 {
                     //NSLog(@"GuestId : %@", [[guestArray objectAtIndex:i] valueForKey:@"guestID"]);
                     //NSLog(@"GuestId : %@", [json valueForKey:@"guestObj"]);
                     
                     //NSLog(@"Name : %@", [[json valueForKey:@"updguest"] valueForKey:@"name"]);
                     
                     
                     NSDictionary *aDictionary = [arrayLess60 objectAtIndex: i];
                     //Make a mutable copy of each dictionary in the array.
                     NSMutableDictionary *mDict = [aDictionary mutableCopy];
                     
                     //Replace the value at key @"key" with some new value @"new value"
                     //mDict[@"name"] = [[json valueForKey:@"updguest"] valueForKey:@"name"]; //Replace this part as needed
                     mDict[@"status"] = [[json valueForKey:@"updguest"] valueForKey:@"status"]; //Replace this part as needed
                     
                     if(![[[json valueForKey:@"updguest"] valueForKey:@"calloutCount"] isEqual:[NSNull null]])
                     {
                         mDict[@"calloutCount"] = [[json valueForKey:@"updguest"] valueForKey:@"calloutCount"];
                     }
                     
                     if(![[[json valueForKey:@"updguest"] valueForKey:@"incompleteParty"] isEqual:[NSNull null]])
                     {
                         mDict[@"incompleteParty"] = [[json valueForKey:@"updguest"] valueForKey:@"incompleteParty"];
                     }
                     
                     [arrayLess60 replaceObjectAtIndex:i withObject:mDict];
                     
                     [tblView60 reloadData];
                     
                     
                 }
             }
             
             // from less 90
             for(int i = 0; i<[arrayLess90 count]; i++)
             {
                 if([[[arrayLess90 objectAtIndex:i] valueForKey:@"guestID"] isEqualToNumber:[json valueForKey:@"guestObj"]])
                 {
                     //NSLog(@"GuestId : %@", [[guestArray objectAtIndex:i] valueForKey:@"guestID"]);
                     //NSLog(@"GuestId : %@", [json valueForKey:@"guestObj"]);
                     
                     //NSLog(@"Name : %@", [[json valueForKey:@"updguest"] valueForKey:@"name"]);
                     
                     
                     NSDictionary *aDictionary = [arrayLess90 objectAtIndex: i];
                     //Make a mutable copy of each dictionary in the array.
                     NSMutableDictionary *mDict = [aDictionary mutableCopy];
                     
                     //Replace the value at key @"key" with some new value @"new value"
                     //mDict[@"name"] = [[json valueForKey:@"updguest"] valueForKey:@"name"]; //Replace this part as needed
                     mDict[@"status"] = [[json valueForKey:@"updguest"] valueForKey:@"status"]; //Replace this part as needed
                     
                     if(![[[json valueForKey:@"updguest"] valueForKey:@"calloutCount"] isEqual:[NSNull null]])
                     {
                         mDict[@"calloutCount"] = [[json valueForKey:@"updguest"] valueForKey:@"calloutCount"];
                     }
                     
                     if(![[[json valueForKey:@"updguest"] valueForKey:@"incompleteParty"] isEqual:[NSNull null]])
                     {
                         mDict[@"incompleteParty"] = [[json valueForKey:@"updguest"] valueForKey:@"incompleteParty"];
                     }
                     
                     [arrayLess90 replaceObjectAtIndex:i withObject:mDict];
                     
                     [tblView90 reloadData];
                     
                     
                 }
             }
             
             */
         }
         else if([[json valueForKey:@"OP"] isEqualToString:@"MARK_AS_SEATED"])
         {
             [self fetchList];
             
             /*
             // from less 30
             for(int i = 0; i<[arrayLess30 count]; i++)
             {
                 if([[[arrayLess30 objectAtIndex:i] valueForKey:@"guestID"] isEqualToNumber:[json valueForKey:@"guestObj"]])
                 {
                     //NSLog(@"GuestId : %@", [[guestArray objectAtIndex:i] valueForKey:@"guestID"]);
                     //NSLog(@"GuestId : %@", [json valueForKey:@"guestObj"]);
                     
                     [arrayLess30 removeObjectAtIndex:i];
                     
                     [tblView30 reloadData];
                 }
             }
             
             // from less 60
             for(int i = 0; i<[arrayLess60 count]; i++)
             {
                 if([[[arrayLess60 objectAtIndex:i] valueForKey:@"guestID"] isEqualToNumber:[json valueForKey:@"guestObj"]])
                 {
                     //NSLog(@"GuestId : %@", [[guestArray objectAtIndex:i] valueForKey:@"guestID"]);
                     //NSLog(@"GuestId : %@", [json valueForKey:@"guestObj"]);
                     
                     [arrayLess60 removeObjectAtIndex:i];
                     
                     [tblView60 reloadData];
                 }
             }
             
             // from less 90
             for(int i = 0; i<[arrayLess90 count]; i++)
             {
                 if([[[arrayLess90 objectAtIndex:i] valueForKey:@"guestID"] isEqualToNumber:[json valueForKey:@"guestObj"]])
                 {
                     //NSLog(@"GuestId : %@", [[guestArray objectAtIndex:i] valueForKey:@"guestID"]);
                     //NSLog(@"GuestId : %@", [json valueForKey:@"guestObj"]);
                     
                     [arrayLess90 removeObjectAtIndex:i];
                     
                     [tblView90 reloadData];
                 }
             }
             */
             
             
         }
         
     }];
}


- (void)fetchList
{
    if(appDelegate.isInternetReachble)
    {
        
        
        
        //{"filters":null,"sort":null,"sortOrder":null,"pageSize":500,"pageNo":1}
        
        //NSError *jsonError;
        NSData *objectData = [@"{\"filters\":null,\"sort\":null,\"sortOrder\":null,\"pageSize\":500,\"pageNo\":1}" dataUsingEncoding:NSUTF8StringEncoding];
        
        
        NSString *jsonString = [[NSString alloc] initWithData:objectData encoding:NSUTF8StringEncoding];
        
        /*NSData *data = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
         id json = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];*/
        
        
        //NSDictionary *json = [NSJSONSerialization JSONObjectWithData:objectData options:NSJSONReadingMutableContainers error:&jsonError];
        
        NSString *urlStr = [NSString stringWithFormat:@"%@web/rest/waitlistRestAction/checkinusers?orgid=%@&pagerReqParam=%@",ServiceUrl,[[NSUserDefaults standardUserDefaults] valueForKey:@"OrgId"],jsonString];
        
        NSString *escapedPath = [urlStr stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
        
        NSURL *url = [NSURL URLWithString:escapedPath];
        NSURLRequest *request = [NSURLRequest requestWithURL:url];
        
        [NSURLConnection sendAsynchronousRequest:request queue:[NSOperationQueue mainQueue] completionHandler:^(NSURLResponse *response, NSData *data, NSError *connectionError)
         {
             
             
             if(data != nil)
             {
                 NSString *htmlSTR = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
                 //NSLog(@"%@",htmlSTR);
                 
                 NSData *data = [htmlSTR dataUsingEncoding:NSUTF8StringEncoding];
                 id json = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
                 
                 //NSDictionary *jsonObject=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:nil];
                 
                 //if([[json valueForKey:@"success"] isEqualToString:@"1"])
                 if(![[json valueForKey:@"status"] isEqual:[NSNull null]])
                 {
                     if([[json valueForKey:@"status"] isEqualToString:@"SUCCESS"])
                     {
                         if(![[json valueForKey:@"serviceResult"] isEqual:[NSNull null]])
                         {
                             NSArray *guestList = [[json valueForKey:@"serviceResult"] valueForKey:@"records"];
                             
                             if(guestList.count > 0)
                             {
                                 if(guestArray.count > 0)
                                 {
                                     [guestArray removeAllObjects];
                                 }
                                 
                                 [guestArray addObjectsFromArray:guestList];
                                 
                                 
                                 if(arrayLess30.count > 0)
                                 {
                                     [arrayLess30 removeAllObjects];
                                 }
                                 if(arrayLess60.count > 0)
                                 {
                                     [arrayLess60 removeAllObjects];
                                 }
                                 if(arrayLess90.count > 0)
                                 {
                                     [arrayLess90 removeAllObjects];
                                 }
                                 
                                 // For Less Than 30
                                 /*for(int i = 0; i < [guestArray count]; i++)
                                  {
                                  if([[[guestArray objectAtIndex:i] valueForKey:@"WaitTime"] integerValue] <= 30)
                                  {
                                  [arrayLess30 addObject:[guestArray objectAtIndex:i]];
                                  }
                                  }*/
                                 
                                 
                                 if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfRows"] isEqualToString:@""])
                                 {
                                     int numberOfRows = [[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfRows"] intValue];
                                     
                                     for(int i = 0; i < [guestArray count]; i++)
                                     {
                                         
                                         [arrayLess30 addObject:[guestArray objectAtIndex:i]];
                                         
                                         
                                         if([arrayLess30 count] == numberOfRows)
                                         {
                                             break;
                                         }
                                     }
                                 }
                                 else
                                 {
                                     for(int i = 0; i < [guestArray count]; i++)
                                     {
                                         
                                         [arrayLess30 addObject:[guestArray objectAtIndex:i]];
                                         
                                         if(i == 24)
                                         {
                                             break;
                                         }
                                     }
                                 }
                                 
                                 /*for(int i = 0; i < [guestArray count]; i++)
                                  {
                                  
                                  [arrayLess30 addObject:[guestArray objectAtIndex:i]];
                                  
                                  if(i == 24)
                                  {
                                  break;
                                  }
                                  }*/
                                 
                                 if(arrayLess30.count > 0)
                                 {
                                     [tblView30 reloadData];
                                     
                                     lblNoUser30.hidden = true;
                                 }
                                 else
                                 {
                                     lblNoUser30.hidden = true;
                                 }
                                 //
                                 
                                 
                                 
                                 // For Less Than 60
                                 /*for(int i = 0; i < [guestArray count]; i++)
                                  {
                                  if([[[guestArray objectAtIndex:i] valueForKey:@"WaitTime"] integerValue] > 30 & [[[guestArray objectAtIndex:i] valueForKey:@"WaitTime"] integerValue] <= 60)
                                  //if([[[guestArray objectAtIndex:i] valueForKey:@"WaitTime"] integerValue] <= 60)
                                  {
                                  [arrayLess60 addObject:[guestArray objectAtIndex:i]];
                                  }
                                  }*/
                                 
                                 if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfRows"] isEqualToString:@""])
                                 {
                                     int numberOfRows = [[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfRows"] intValue];
                                     
                                     int rows = (int)[arrayLess30 count];
                                     
                                     for(int i = rows; i < [guestArray count]; i++)
                                     {
                                         
                                         [arrayLess60 addObject:[guestArray objectAtIndex:i]];
                                         
                                         if([arrayLess60 count] == numberOfRows)
                                         {
                                             break;
                                         }
                                         
                                     }
                                 }
                                 else
                                 {
                                     for(int i = 25; i < [guestArray count]; i++)
                                     {
                                         
                                         [arrayLess60 addObject:[guestArray objectAtIndex:i]];
                                         
                                         if(i == 49)
                                         {
                                             break;
                                         }
                                         
                                     }
                                 }
                                 
                                 /*for(int i = 25; i < [guestArray count]; i++)
                                  {
                                  
                                  [arrayLess60 addObject:[guestArray objectAtIndex:i]];
                                  
                                  if(i == 49)
                                  {
                                  break;
                                  }
                                  
                                  }*/
                                 
                                 if(arrayLess60.count > 0)
                                 {
                                     [tblView60 reloadData];
                                     
                                     lblNoUser60.hidden = true;
                                 }
                                 else
                                 {
                                     lblNoUser60.hidden = true;
                                 }
                                 //
                                 
                                 
                                 // For Less Than 90
                                 /*for(int i = 0; i < [guestArray count]; i++)
                                  {
                                  if([[[guestArray objectAtIndex:i] valueForKey:@"WaitTime"] integerValue] > 60 & [[[guestArray objectAtIndex:i] valueForKey:@"WaitTime"] integerValue] <= 90)
                                  //if([[[guestArray objectAtIndex:i] valueForKey:@"WaitTime"] integerValue] <= 90)
                                  {
                                  [arrayLess90 addObject:[guestArray objectAtIndex:i]];
                                  }
                                  }*/
                                 
                                 
                                 if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfRows"] isEqualToString:@""])
                                 {
                                     int numberOfRows = [[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfRows"] intValue];
                                     
                                     int rows1 = (int)[arrayLess30 count];
                                     int rows2 = (int)[arrayLess60 count];
                                     
                                     int row = rows1 + rows2;
                                     
                                     for(int i = row; i < [guestArray count]; i++)
                                     {
                                         [arrayLess90 addObject:[guestArray objectAtIndex:i]];
                                         
                                         if([arrayLess90 count] == numberOfRows)
                                         {
                                             break;
                                         }
                                         
                                     }
                                 }
                                 else
                                 {
                                     for(int i = 50; i < [guestArray count]; i++)
                                     {
                                         [arrayLess90 addObject:[guestArray objectAtIndex:i]];
                                         
                                         if(i == 74)
                                         {
                                             break;
                                         }
                                         
                                     }
                                 }
                                 
                                 /*for(int i = 50; i < [guestArray count]; i++)
                                  {
                                  [arrayLess90 addObject:[guestArray objectAtIndex:i]];
                                  
                                  if(i == 74)
                                  {
                                  break;
                                  }
                                  
                                  }*/
                                 
                                 if(arrayLess90.count > 0)
                                 {
                                     [tblView90 reloadData];
                                     
                                     lblNoUser90.hidden = true;
                                 }
                                 else
                                 {
                                     lblNoUser90.hidden = true;
                                 }
                                 //
                                 
                                 [tblView30 reloadData];
                                 [tblView60 reloadData];
                                 [tblView90 reloadData];
                             }
                             else
                             {
                                 UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:@"There is no guest checked in yet." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                                 
                                 [alert show];
                             }
                         }
                         else
                         {
                             /*UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:@"There is no guest checked in yet." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                              
                              [alert show];*/
                         }
                         
                         //[imgResto sd_setImageWithURL:[NSURL URLWithString:[[json valueForKey:@"serviceResult"]valueForKey:@"imageOrgPath"]] placeholderImage:[UIImage imageNamed:@"RestoImage"]];
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
                 else
                 {
                     if(arrayLess30.count > 0)
                     {
                         [arrayLess30 removeAllObjects];
                     }
                     if(arrayLess60.count > 0)
                     {
                         [arrayLess60 removeAllObjects];
                     }
                     if(arrayLess90.count > 0)
                     {
                         [arrayLess90 removeAllObjects];
                     }
                     
                     [tblView30 reloadData];
                     [tblView60 reloadData];
                     [tblView90 reloadData];
                 }
             }
         }];
    }
    else
    {
        /*UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:@"Please check your internet connection." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
         
         [alert show];*/
    }
    
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    
    if (alertView.tag == 101)
    {
        if(buttonIndex == 1)
        {
            [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"fromBack"];
            [[NSUserDefaults standardUserDefaults] synchronize];
            
            [self.navigationController popToRootViewControllerAnimated:YES];
        }
        
        if(buttonIndex == 2)
        {
            [self fetchGuestList];
        }
        
        if(buttonIndex == 3)
        {
            
            [txtColumns.layer setBorderWidth:1.5];
            [txtColumns.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
            
            [txtRows.layer setBorderWidth:1.5];
            [txtRows.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
            
            [txtColumnOne.layer setBorderWidth:1.5];
            [txtColumnOne.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
            
            [txtColumnTwo.layer setBorderWidth:1.5];
            [txtColumnTwo.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
            
            [txtColumnThree.layer setBorderWidth:1.5];
            [txtColumnThree.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
            
            [txtColumnFour.layer setBorderWidth:1.5];
            [txtColumnFour.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
            
            
            txtColumns.text = @"";
            txtRows.text = @"";
            txtColumnOne.text = @"";
            txtColumnTwo.text = @"";
            txtColumnThree.text = @"";
            txtColumnFour.text = @"";
            
            _heightViewSubSettings.constant = 700;
            
            txtColumnOne.hidden = false;
            txtColumnTwo.hidden = false;
            txtColumnThree.hidden = false;
            txtColumnFour.hidden = false;
            
            txtColumns.hidden = false;
            btnColumns.hidden = false;
            txtRows.hidden = false;
            btnRows.hidden = false;
            
            txtColumnOne.placeholder = @"Enter first column name";
            txtColumnTwo.placeholder = @"Enter second column name";
            txtColumnThree.placeholder = @"Enter third column name";
            txtColumnFour.placeholder = @"Enter fourth column name";
            
            [[NSUserDefaults standardUserDefaults] setValue:lblColumnOneName.text forKey:@"columnOneName"];
            [[NSUserDefaults standardUserDefaults] setValue:lblColumnTwoName.text forKey:@"columnTwoName"];
            [[NSUserDefaults standardUserDefaults] setValue:lblColumnThreeName.text forKey:@"columnThreeName"];
            [[NSUserDefaults standardUserDefaults] synchronize];
            
            
            /*if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfColumns"] isEqualToString:@""])
            {
                txtColumns.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfColumns"];
                
            }
            if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfRows"] isEqualToString:@""])
            {
                txtRows.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfRows"];
                
            }*/
            
            if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"columnOneName"] isEqualToString:@""])
            {
                txtColumnOne.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnOneName"];
                
            }
            if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"columnTwoName"] isEqualToString:@""])
            {
                txtColumnTwo.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnTwoName"];
                
            }
            if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"columnThreeName"] isEqualToString:@""])
            {
                txtColumnThree.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnThreeName"];
            }
            if(![[[NSUserDefaults standardUserDefaults] valueForKey:@"columnFourName"] isEqualToString:@""])
            {
                txtColumnFour.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnFourName"];
            }
            
            _txtColToTop.constant = 25;
            
            
            if([[NSUserDefaults standardUserDefaults] boolForKey:@"showNotPresent"] == YES)
            {
                btnNotPresent.selected = YES;
                [btnNotPresent setBackgroundColor:[UIColor colorWithRed:173.0/255.0 green:22.0/255.0 blue:45.0/255.0 alpha:1.0]];
            }
            else
            {
                [btnNotPresent setBackgroundColor:[UIColor whiteColor]];
            }
            
            if([[NSUserDefaults standardUserDefaults] boolForKey:@"showIncomplete"] == YES)
            {
                btnIncomplete.selected = YES;
                [btnIncomplete setBackgroundColor:[UIColor colorWithRed:255.0/255.0 green:255.0/255.0 blue:102.0/255.0 alpha:1.0]];
            }
            else
            {
                [btnIncomplete setBackgroundColor:[UIColor whiteColor]];
            }
            
            if([[NSUserDefaults standardUserDefaults] boolForKey:@"showParty"] == YES)
            {
                btnShowParty.selected = YES;
                [btnShowParty setBackgroundColor:[UIColor darkGrayColor]];
            }
            else
            {
                [btnShowParty setBackgroundColor:[UIColor whiteColor]];
            }
            
            viewSettings.hidden = false;
        }
    }
    
}

- (IBAction)btnCloseSettings_clicked:(id)sender
{
    viewSettings.hidden = true;
    
    [txtFieldRef resignFirstResponder];
    
    [tblView30 reloadData];
    [tblView60 reloadData];
    [tblView90 reloadData];
}


- (IBAction)btnColumns_clicked:(id)sender
{
    btnColumns.selected = true;
    btnRows.selected = false;
    
    tblViewColumnsAndRows.hidden = false;
    [tblViewColumnsAndRows reloadData];
    
    _tblColRowToTop.constant = 0;
    _heightTblColRow.constant = 96 ;
}

- (IBAction)btnRows_clicked:(id)sender
{
    if(![txtColumns.text isEqualToString:@""])
    {
        btnColumns.selected = false;
        btnRows.selected = true;
        
        tblViewColumnsAndRows.hidden = false;
        [tblViewColumnsAndRows reloadData];
        
        _tblColRowToTop.constant = 80;
        _heightTblColRow.constant = 172;
    }
    else
    {
        UIAlertController* alert = [UIAlertController alertControllerWithTitle:@"Kyobee"
                                                                       message:@"Please select number of columns."
                                                                preferredStyle:UIAlertControllerStyleAlert];
        
        
        UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                                              handler:^(UIAlertAction * action) {}];
        
        [alert addAction:defaultAction];
        [self presentViewController:alert animated:YES completion:nil];
    }
    
    
}



- (IBAction)btnSettingsOK_clicked:(id)sender
{
    if(![txtColumns.text isEqualToString:@""] & ![txtRows.text isEqualToString:@""])
    {
        
        if([txtColumns.text isEqualToString:@"1"])
        {
            if(![txtColumnOne.text isEqualToString:@""])
            {
                NSString *columnOneName = [txtColumnOne.text uppercaseString];
                txtColumnOne.text =  columnOneName;
                
                NSString *columnTwoName = [txtColumnTwo.text uppercaseString];
                txtColumnTwo.text =  columnTwoName;
                
                NSString *columnThreeName = [txtColumnThree.text uppercaseString];
                txtColumnThree.text =  columnThreeName;
                
                [[NSUserDefaults standardUserDefaults] setValue:txtColumns.text forKey:@"numberOfColumns"];
                [[NSUserDefaults standardUserDefaults] setValue:txtRows.text forKey:@"numberOfRows"];
                [[NSUserDefaults standardUserDefaults] setValue:txtColumnOne.text forKey:@"columnOneName"];
                [[NSUserDefaults standardUserDefaults] setValue:txtColumnTwo.text forKey:@"columnTwoName"];
                [[NSUserDefaults standardUserDefaults] setValue:txtColumnThree.text forKey:@"columnThreeName"];
                [[NSUserDefaults standardUserDefaults] synchronize];
                
                
                
                lblColumnOneName.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnOneName"];
                lblColumnTwoName.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnTwoName"];
                lblColumnThreeName.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnThreeName"];
                
                viewSettings.hidden = true;
                
                [txtFieldRef resignFirstResponder];
                
                GuestListOneColumn *guestListOneColumn = [[GuestListOneColumn alloc] initWithNibName:@"GuestListOneColumn" bundle:nil];
                [self.navigationController pushViewController:guestListOneColumn animated:NO];
            }
            else
            {
                
                if([txtColumnOne.text isEqualToString:@""])
                {
                    [txtColumnOne.layer setBorderWidth:1.5];
                    [txtColumnOne.layer setBorderColor:[UIColor redColor].CGColor];
                }
                else
                {
                    [txtColumnOne.layer setBorderWidth:1.5];
                    [txtColumnOne.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
                }
            }
            
        }
        
        if([txtColumns.text isEqualToString:@"2"])
        {
            
            if(![txtColumnOne.text isEqualToString:@""] & ![txtColumnTwo.text isEqualToString:@""])
            {
                
                NSString *columnOneName = [txtColumnOne.text uppercaseString];
                txtColumnOne.text =  columnOneName;
                
                NSString *columnTwoName = [txtColumnTwo.text uppercaseString];
                txtColumnTwo.text =  columnTwoName;
                
                NSString *columnThreeName = [txtColumnThree.text uppercaseString];
                txtColumnThree.text =  columnThreeName;
                
                [[NSUserDefaults standardUserDefaults] setValue:txtColumns.text forKey:@"numberOfColumns"];
                [[NSUserDefaults standardUserDefaults] setValue:txtRows.text forKey:@"numberOfRows"];
                [[NSUserDefaults standardUserDefaults] setValue:txtColumnOne.text forKey:@"columnOneName"];
                [[NSUserDefaults standardUserDefaults] setValue:txtColumnTwo.text forKey:@"columnTwoName"];
                [[NSUserDefaults standardUserDefaults] setValue:txtColumnThree.text forKey:@"columnThreeName"];
                [[NSUserDefaults standardUserDefaults] synchronize];
                
                
                
                lblColumnOneName.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnOneName"];
                lblColumnTwoName.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnTwoName"];
                lblColumnThreeName.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnThreeName"];
                
                viewSettings.hidden = true;
                
                [txtFieldRef resignFirstResponder];
                
                GuestListTwoColumn *guestListTwoColumn = [[GuestListTwoColumn alloc] initWithNibName:@"GuestListTwoColumn" bundle:nil];
                [self.navigationController pushViewController:guestListTwoColumn animated:NO];
            }
            else
            {
                if([txtColumnOne.text isEqualToString:@""])
                {
                    [txtColumnOne.layer setBorderWidth:1.5];
                    [txtColumnOne.layer setBorderColor:[UIColor redColor].CGColor];
                }
                else
                {
                    [txtColumnOne.layer setBorderWidth:1.5];
                    [txtColumnOne.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
                }
                
                if([txtColumnTwo.text isEqualToString:@""])
                {
                    [txtColumnTwo.layer setBorderWidth:1.5];
                    [txtColumnTwo.layer setBorderColor:[UIColor redColor].CGColor];
                }
                else
                {
                    [txtColumnTwo.layer setBorderWidth:1.5];
                    [txtColumnTwo.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
                }
            }
            
        }
        
        if([txtColumns.text isEqualToString:@"3"])
        {
            if(![txtColumnOne.text isEqualToString:@""] & ![txtColumnTwo.text isEqualToString:@""] & ![txtColumnThree.text isEqualToString:@""])
            {
                
                NSString *columnOneName = [txtColumnOne.text uppercaseString];
                txtColumnOne.text =  columnOneName;
                
                NSString *columnTwoName = [txtColumnTwo.text uppercaseString];
                txtColumnTwo.text =  columnTwoName;
                
                NSString *columnThreeName = [txtColumnThree.text uppercaseString];
                txtColumnThree.text =  columnThreeName;
               
                [[NSUserDefaults standardUserDefaults] setValue:txtColumns.text forKey:@"numberOfColumns"];
                [[NSUserDefaults standardUserDefaults] setValue:txtRows.text forKey:@"numberOfRows"];
                [[NSUserDefaults standardUserDefaults] setValue:txtColumnOne.text forKey:@"columnOneName"];
                [[NSUserDefaults standardUserDefaults] setValue:txtColumnTwo.text forKey:@"columnTwoName"];
                [[NSUserDefaults standardUserDefaults] setValue:txtColumnThree.text forKey:@"columnThreeName"];
                [[NSUserDefaults standardUserDefaults] synchronize];
                
                
                
                lblColumnOneName.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnOneName"];
                lblColumnTwoName.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnTwoName"];
                lblColumnThreeName.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnThreeName"];
                
                if([[NSUserDefaults standardUserDefaults] boolForKey:@"showParty"] == NO)
                {
                    lblPartyOne.hidden = true;
                    lblPartyTwo.hidden = true;
                    lblPartyThree.hidden = true;
                    
                    _colOneGuestLead.constant = -25;
                    _colTwoGuestLead.constant = -25;
                    _colThreeGuestLead.constant = -25;
                }
                else
                {
                    lblPartyOne.hidden = false;
                    lblPartyTwo.hidden = false;
                    lblPartyThree.hidden = false;
                    
                    _colOneGuestLead.constant = 25;
                    _colTwoGuestLead.constant = 25;
                    _colThreeGuestLead.constant = 25;
                }
                
                
                viewSettings.hidden = true;
                
                [txtFieldRef resignFirstResponder];
                
                [self fetchGuestList];
                
            }
            else
            {
                if([txtColumnOne.text isEqualToString:@""])
                {
                    [txtColumnOne.layer setBorderWidth:1.5];
                    [txtColumnOne.layer setBorderColor:[UIColor redColor].CGColor];
                }
                else
                {
                    [txtColumnOne.layer setBorderWidth:1.5];
                    [txtColumnOne.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
                }
                
                if([txtColumnTwo.text isEqualToString:@""])
                {
                    [txtColumnTwo.layer setBorderWidth:1.5];
                    [txtColumnTwo.layer setBorderColor:[UIColor redColor].CGColor];
                }
                else
                {
                    [txtColumnTwo.layer setBorderWidth:1.5];
                    [txtColumnTwo.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
                }
                
                if([txtColumnThree.text isEqualToString:@""])
                {
                    [txtColumnThree.layer setBorderWidth:1.5];
                    [txtColumnThree.layer setBorderColor:[UIColor redColor].CGColor];
                }
                else
                {
                    [txtColumnThree.layer setBorderWidth:1.5];
                    [txtColumnThree.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
                }
            }
            
        }
        
        if([txtColumns.text isEqualToString:@"4"])
        {
            if(![txtColumnOne.text isEqualToString:@""] & ![txtColumnTwo.text isEqualToString:@""] & ![txtColumnThree.text isEqualToString:@""] & ![txtColumnFour.text isEqualToString:@""])
            {
                
                NSString *columnOneName = [txtColumnOne.text uppercaseString];
                txtColumnOne.text =  columnOneName;
                
                NSString *columnTwoName = [txtColumnTwo.text uppercaseString];
                txtColumnTwo.text =  columnTwoName;
                
                NSString *columnThreeName = [txtColumnThree.text uppercaseString];
                txtColumnThree.text =  columnThreeName;
                
                NSString *columnFourName = [txtColumnFour.text uppercaseString];
                txtColumnFour.text =  columnFourName;
                
                
                [[NSUserDefaults standardUserDefaults] setValue:txtColumns.text forKey:@"numberOfColumns"];
                [[NSUserDefaults standardUserDefaults] setValue:txtRows.text forKey:@"numberOfRows"];
                [[NSUserDefaults standardUserDefaults] setValue:txtColumnOne.text forKey:@"columnOneName"];
                [[NSUserDefaults standardUserDefaults] setValue:txtColumnTwo.text forKey:@"columnTwoName"];
                [[NSUserDefaults standardUserDefaults] setValue:txtColumnThree.text forKey:@"columnThreeName"];
                [[NSUserDefaults standardUserDefaults] setValue:txtColumnFour.text forKey:@"columnFourName"];
                [[NSUserDefaults standardUserDefaults] synchronize];
                
                
                
                lblColumnOneName.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnOneName"];
                lblColumnTwoName.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnTwoName"];
                lblColumnThreeName.text = [[NSUserDefaults standardUserDefaults] valueForKey:@"columnThreeName"];

                
                
                viewSettings.hidden = true;
                
                [txtFieldRef resignFirstResponder];
                
                //[self fetchGuestList];
                
                GuestListFourthColumn *guestListFourthColumn = [[GuestListFourthColumn alloc] initWithNibName:@"GuestListFourthColumn" bundle:nil];
                [self.navigationController pushViewController:guestListFourthColumn animated:NO];
                
            }
            else
            {
                if([txtColumnOne.text isEqualToString:@""])
                {
                    [txtColumnOne.layer setBorderWidth:1.5];
                    [txtColumnOne.layer setBorderColor:[UIColor redColor].CGColor];
                }
                else
                {
                    [txtColumnOne.layer setBorderWidth:1.5];
                    [txtColumnOne.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
                }
                
                if([txtColumnTwo.text isEqualToString:@""])
                {
                    [txtColumnTwo.layer setBorderWidth:1.5];
                    [txtColumnTwo.layer setBorderColor:[UIColor redColor].CGColor];
                }
                else
                {
                    [txtColumnTwo.layer setBorderWidth:1.5];
                    [txtColumnTwo.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
                }
                
                if([txtColumnThree.text isEqualToString:@""])
                {
                    [txtColumnThree.layer setBorderWidth:1.5];
                    [txtColumnThree.layer setBorderColor:[UIColor redColor].CGColor];
                }
                else
                {
                    [txtColumnThree.layer setBorderWidth:1.5];
                    [txtColumnThree.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
                }
                
                if([txtColumnFour.text isEqualToString:@""])
                {
                    [txtColumnFour.layer setBorderWidth:1.5];
                    [txtColumnFour.layer setBorderColor:[UIColor redColor].CGColor];
                }
                else
                {
                    [txtColumnFour.layer setBorderWidth:1.5];
                    [txtColumnFour.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
                }
            }
            
        }
    }
    else
    {
        
        if([txtColumns.text isEqualToString:@""])
        {
            [txtColumns.layer setBorderWidth:1.5];
            [txtColumns.layer setBorderColor:[UIColor redColor].CGColor];
        }
        else
        {
            [txtColumns.layer setBorderWidth:1.5];
            [txtColumns.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
        }
        
        if([txtRows.text isEqualToString:@""])
        {
            [txtRows.layer setBorderWidth:1.5];
            [txtRows.layer setBorderColor:[UIColor redColor].CGColor];
        }
        else
        {
            [txtRows.layer setBorderWidth:1.5];
            [txtRows.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
        }
    }
}

- (IBAction)btnNotPresent_clicked:(id)sender
{
    btnNotPresent.selected = !btnNotPresent.selected;
    
    if(btnNotPresent.selected)
    {
        [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"showNotPresent"];
        [[NSUserDefaults standardUserDefaults]synchronize];
        
        [btnNotPresent setBackgroundColor:[UIColor colorWithRed:173.0/255.0 green:22.0/255.0 blue:45.0/255.0 alpha:1.0]];
        
        //[btn_Remember.layer setBorderColor:[UIColor colorWithRed:158.0/255.0 green:151.0/255.0 blue:141.0/255.0 alpha:1.0].CGColor];
    }
    else
    {
        [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"showNotPresent"];
        [[NSUserDefaults standardUserDefaults]synchronize];
        
        [btnNotPresent setBackgroundColor:[UIColor whiteColor]];
    }
}

- (IBAction)btnIncomplete_clicked:(id)sender
{
    btnIncomplete.selected = !btnIncomplete.selected;
    
    if(btnIncomplete.selected)
    {
        [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"showIncomplete"];
        [[NSUserDefaults standardUserDefaults]synchronize];
        
        [btnIncomplete setBackgroundColor:[UIColor colorWithRed:255.0/255.0 green:255.0/255.0 blue:102.0/255.0 alpha:1.0]];
        
        //[btn_Remember.layer setBorderColor:[UIColor colorWithRed:158.0/255.0 green:151.0/255.0 blue:141.0/255.0 alpha:1.0].CGColor];
    }
    else
    {
        [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"showIncomplete"];
        [[NSUserDefaults standardUserDefaults]synchronize];
        
        [btnIncomplete setBackgroundColor:[UIColor whiteColor]];
    }
    
}

- (IBAction)btnShowParty_clicked:(id)sender
{
    btnShowParty.selected = !btnShowParty.selected;
    
    if(btnShowParty.selected)
    {
        [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"showParty"];
        [[NSUserDefaults standardUserDefaults]synchronize];
        
        [btnShowParty setBackgroundColor:[UIColor darkGrayColor]];
        
        //[btn_Remember.layer setBorderColor:[UIColor colorWithRed:158.0/255.0 green:151.0/255.0 blue:141.0/255.0 alpha:1.0].CGColor];
    }
    else
    {
        [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"showParty"];
        [[NSUserDefaults standardUserDefaults]synchronize];
        
        [btnShowParty setBackgroundColor:[UIColor whiteColor]];
    }
}

#pragma mark -
#pragma mark - Text Field Delegate Methods

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    txtFieldRef = textField;
    
    tblViewColumnsAndRows.hidden = true;
    
    UIInterfaceOrientation orientation = [UIApplication sharedApplication].statusBarOrientation;
    
    if (UIInterfaceOrientationIsLandscape(orientation))
    {
        txtColumns.hidden = true;
        btnColumns.hidden = true;
        txtRows.hidden = true;
        btnRows.hidden = true;
        
        if(txtFieldRef == txtColumnOne)
        {
            _txtColToTop.constant = -140;
        }
        
        if(txtFieldRef == txtColumnTwo)
        {
            _txtColToTop.constant = -140;
        }
        
        if(txtFieldRef == txtColumnThree)
        {
            _txtColToTop.constant = -220;
        }
        
        if(txtFieldRef == txtColumnFour)
        {
            _txtColToTop.constant = -220;
        }
    }
    else
    {
        
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
    
    if(textField == txtColumnOne)
    {
        [txtColumnOne.layer setBorderWidth:1.5];
        [txtColumnOne.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
        
        
        return YES;
       
    }
    else if (textField == txtColumnTwo)
    {
        [txtColumnTwo.layer setBorderWidth:1.5];
        [txtColumnTwo.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
        
        
        
        return YES;
    }
    else
    {
        [txtColumnThree.layer setBorderWidth:1.5];
        [txtColumnThree.layer setBorderColor:[UIColor colorWithRed:195.0/255.0 green:195.0/255.0 blue:195.0/255.0 alpha:1.0].CGColor];
        
        return YES;
    }
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
        _txtColToTop.constant = 25;
        
        txtColumns.hidden = false;
        btnColumns.hidden = false;
        txtRows.hidden = false;
        btnRows.hidden = false;
        
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
        
        _txtColToTop.constant = 25;
        
        txtColumns.hidden = false;
        btnColumns.hidden = false;
        txtRows.hidden = false;
        btnRows.hidden = false;
        
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
    
    txtColumns.hidden = false;
    btnColumns.hidden = false;
    txtRows.hidden = false;
    btnRows.hidden = false;
    
    _txtColToTop.constant = 25;
    
    tblViewColumnsAndRows.hidden = true;
    
    [txtFieldRef resignFirstResponder];
    
    [self.view setNeedsUpdateConstraints];
    
    [UIView animateWithDuration:0.40f animations:^{
        [self.view layoutIfNeeded];
    }];
}



@end
