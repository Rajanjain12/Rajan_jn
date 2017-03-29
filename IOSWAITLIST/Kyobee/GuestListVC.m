//
//  GuestListVC.m
//  Kyobee
//
//  Created by Mayur Pandya on 11/02/17.
//
//

#import "GuestListVC.h"

///*** web image
#import "UIImageView+WebCache.h"
//****

#import "GuestListVCCell.h"

@interface GuestListVC ()

@end

@implementation GuestListVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    appDelegate =(AppDelegate*)[[UIApplication sharedApplication]delegate];
    
    guestArray = [[NSMutableArray alloc] initWithCapacity:0];
    
    // Realtime
    _ortcClient = [OrtcClient ortcClientWithConfig:self];
    [_ortcClient setClusterUrl:@"https://ortc-developers.realtime.co/server/ssl/2.1"];
    [_ortcClient connect:@"j9MLMa" authenticationToken:@"testToken"];
    //
    
    
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
        
        [imgResto sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/static/orglogos/%@",[[NSUserDefaults standardUserDefaults] valueForKey:@"logofile name"]]] placeholderImage:[UIImage imageNamed:@"RestoImage"] options:SDWebImageRefreshCached];
    }
    
    
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
        
        NSString *urlStr = [NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/kyobee/web/rest/waitlistRestAction/checkinusers?orgid=%@&pagerReqParam=%@",[[NSUserDefaults standardUserDefaults] valueForKey:@"OrgId"],jsonString];
        
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
                                 
                                 [tblViewGuestList reloadData];
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
                     if(guestArray.count > 0)
                     {
                         [guestArray removeAllObjects];
                     }
                     
                     [tblViewGuestList reloadData];
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


- (void)loadTimeMatrics
{
    if(appDelegate.isInternetReachble)
    {

        NSString *urlStr = [NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/kyobee/web/rest/waitlistRestAction/totalwaittimemetricks?orgid=%@",[[NSUserDefaults standardUserDefaults] valueForKey:@"OrgId"]];
        
        
        NSURL *url = [NSURL URLWithString:urlStr];
        
        
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
                 if([[json valueForKey:@"status"] isEqualToString:@"SUCCESS"])
                 {

                     [imgResto sd_setImageWithURL:[NSURL URLWithString:[[json valueForKey:@"serviceResult"]valueForKey:@"imageOrgPath"]] placeholderImage:[UIImage imageNamed:@"RestoImage"]];
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
        /*UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Kyobee" message:@"Please check your internet connection." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        
        [alert show];*/
    }
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



#pragma mark - UITableViewDataSource


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(guestArray.count >= 20)
    {
        return 20;
    }
    else
    {
        return guestArray.count;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 41;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    static NSString *CellIdentifier = @"Cell";
    
    GuestListVCCell *cell = (GuestListVCCell *)[tblViewGuestList dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil)
    {
        cell = [[[NSBundle mainBundle]loadNibNamed:NSStringFromClass([GuestListVCCell class]) owner:nil options:nil] lastObject];
    }
    
    cell.lblRank.text = [NSString stringWithFormat:@"%@",[[guestArray objectAtIndex:indexPath.row] valueForKey:@"rank"]];
    cell.lblGuestName.text = [NSString stringWithFormat:@"%@",[[guestArray objectAtIndex:indexPath.row] valueForKey:@"name"]];
    //cell.lblStatus.text = [NSString stringWithFormat:@"%@",[[guestArray objectAtIndex:indexPath.row] valueForKey:@"status"]];

    
    if([[[guestArray objectAtIndex:indexPath.row] valueForKey:@"calloutCount"] isEqual:[NSNull null]] & [[[guestArray objectAtIndex:indexPath.row]valueForKey:@"incompleteParty"] isEqual:[NSNull null]])
    {
        cell.lblStatus.text = @"Checkin";
    }
    else if (![[[guestArray objectAtIndex:indexPath.row]valueForKey:@"calloutCount"] isEqual:[NSNull null]] & ![[[guestArray objectAtIndex:indexPath.row]valueForKey:@"incompleteParty"] isEqual:[NSNull null]])
    {
        cell.lblStatus.text = @"Incomplete / Not Present";
        
        cell.lblStatus.font = [UIFont systemFontOfSize:22.0];
    }
    else if (![[[guestArray objectAtIndex:indexPath.row]valueForKey:@"calloutCount"] isEqual:[NSNull null]] & [[[guestArray objectAtIndex:indexPath.row]valueForKey:@"incompleteParty"] isEqual:[NSNull null]])
    {
        cell.lblStatus.text = @"Not Present";
    }
    else if ([[[guestArray objectAtIndex:indexPath.row]valueForKey:@"calloutCount"] isEqual:[NSNull null]] & ![[[guestArray objectAtIndex:indexPath.row]valueForKey:@"incompleteParty"] isEqual:[NSNull null]])
    {
        cell.lblStatus.text = @"Incomplete";
    }
    
    
    
    
    cell.backgroundColor = [UIColor clearColor];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    return cell;
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)btnLogout_clicked:(id)sender
{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Kyobee" message:@"Are you sure you want to logout?" delegate:self cancelButtonTitle:@"NO" otherButtonTitles:@"YES", nil];
    
    alert.tag = 101;
    
    [alert show];
}

- (IBAction)btnRefresh_clicked:(id)sender
{
    [self fetchGuestList];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    
    if (alertView.tag == 101)
    {
        if(buttonIndex == 1)
        {
            [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"rememberMe"];
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"displaymodeselected"];
            [[NSUserDefaults standardUserDefaults]synchronize];
            
            [self.navigationController popToRootViewControllerAnimated:YES];
        }
    }
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
             for(int i = 0; i<[guestArray count]; i++)
             {
                 if([[[guestArray objectAtIndex:i] valueForKey:@"guestID"] isEqualToNumber:[json valueForKey:@"guestObj"]])
                 {
                     //NSLog(@"GuestId : %@", [[guestArray objectAtIndex:i] valueForKey:@"guestID"]);
                     //NSLog(@"GuestId : %@", [json valueForKey:@"guestObj"]);
                     
                     //NSLog(@"Name : %@", [[json valueForKey:@"updguest"] valueForKey:@"name"]);
                     
                     
                     NSDictionary *aDictionary = [guestArray objectAtIndex: i];
                     //Make a mutable copy of each dictionary in the array.
                     NSMutableDictionary *mDict = [aDictionary mutableCopy];
                     
                     //Replace the value at key @"key" with some new value @"new value"
                     mDict[@"name"] = [[json valueForKey:@"updguest"] valueForKey:@"name"]; //Replace this part as needed
                     mDict[@"status"] = [[json valueForKey:@"updguest"] valueForKey:@"status"]; //Replace this part as needed
                     
                     
                     
                     
                     [guestArray replaceObjectAtIndex:i withObject:mDict];
                     
                     [tblViewGuestList reloadData];
                 }
             }
         }
         else if([[json valueForKey:@"OP"] isEqualToString:@"UPD"])
         {
             
             jsonForNotPresentAndIcomplete = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
             
             
             for(int i = 0; i<[guestArray count]; i++)
             {
                 if([[[guestArray objectAtIndex:i] valueForKey:@"guestID"] isEqualToNumber:[json valueForKey:@"guestObj"]])
                 {
                     //NSLog(@"GuestId : %@", [[guestArray objectAtIndex:i] valueForKey:@"guestID"]);
                     //NSLog(@"GuestId : %@", [json valueForKey:@"guestObj"]);
                     
                     //NSLog(@"Name : %@", [[json valueForKey:@"updguest"] valueForKey:@"name"]);
                     
                     
                     NSDictionary *aDictionary = [guestArray objectAtIndex: i];
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
                     
                     [guestArray replaceObjectAtIndex:i withObject:mDict];
                     
                     [tblViewGuestList reloadData];
                     
                     
                 }
             }
         }
         else if([[json valueForKey:@"OP"] isEqualToString:@"MARK_AS_SEATED"])
         {
             for(int i = 0; i<[guestArray count]; i++)
             {
                 if([[[guestArray objectAtIndex:i] valueForKey:@"guestID"] isEqualToNumber:[json valueForKey:@"guestObj"]])
                 {
                     //NSLog(@"GuestId : %@", [[guestArray objectAtIndex:i] valueForKey:@"guestID"]);
                     //NSLog(@"GuestId : %@", [json valueForKey:@"guestObj"]);
                     
                     [guestArray removeObjectAtIndex:i];
                     
                     [tblViewGuestList reloadData];
                 }
             }
         }
         /*else if([[json valueForKey:@"OP"] isEqualToString:@"NOT_PRESENT"])
         {
             for(int i = 0; i<[guestArray count]; i++)
             {
                 if([[[guestArray objectAtIndex:i] valueForKey:@"guestID"] isEqualToNumber:[jsonForNotPresentAndIcomplete valueForKey:@"guestObj"]])
                 {
                     
                     NSDictionary *aDictionary = [guestArray objectAtIndex: i];
                     //Make a mutable copy of each dictionary in the array.
                     NSMutableDictionary *mDict = [aDictionary mutableCopy];
                     
                     //Replace the value at key @"key" with some new value @"new value"
                     //mDict[@"name"] = [[json valueForKey:@"updguest"] valueForKey:@"name"]; //Replace this part as needed
                     mDict[@"status"] = [[json valueForKey:@"updguest"] valueForKey:@"status"]; //Replace this part as needed
                     
                     if(![[[json valueForKey:@"updguest"] valueForKey:@"calloutCount"] isEqual:[NSNull null]])
                     {
                         mDict[@"calloutCount"] = [[json valueForKey:@"updguest"] valueForKey:@"calloutCount"];
                     }
                     
                     [guestArray replaceObjectAtIndex:i withObject:mDict];
                     
                     [tblViewGuestList reloadData];
                     
                     
                 }
             }
         }
         else if([[json valueForKey:@"OP"] isEqualToString:@"INCOMPLETE"])
         {
             for(int i = 0; i<[guestArray count]; i++)
             {
                 if([[[guestArray objectAtIndex:i] valueForKey:@"guestID"] isEqualToNumber:[jsonForNotPresentAndIcomplete valueForKey:@"guestObj"]])
                 {
                     
                     NSDictionary *aDictionary = [guestArray objectAtIndex: i];
                     //Make a mutable copy of each dictionary in the array.
                     NSMutableDictionary *mDict = [aDictionary mutableCopy];
                     
                     //Replace the value at key @"key" with some new value @"new value"
                     //mDict[@"name"] = [[json valueForKey:@"updguest"] valueForKey:@"name"]; //Replace this part as needed
                     mDict[@"status"] = [[json valueForKey:@"updguest"] valueForKey:@"status"]; //Replace this part as needed
                     
                     if(![[[json valueForKey:@"updguest"] valueForKey:@"incompleteParty"] isEqual:[NSNull null]])
                      {
                      mDict[@"incompleteParty"] = [[json valueForKey:@"updguest"] valueForKey:@"incompleteParty"];
                      }
                     
                     [guestArray replaceObjectAtIndex:i withObject:mDict];
                     
                     [tblViewGuestList reloadData];
                     
                     
                 }
             }
         }*/
         
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
        
        NSString *urlStr = [NSString stringWithFormat:@"http://jbossdev-kyobee.rhcloud.com/kyobee/web/rest/waitlistRestAction/checkinusers?orgid=%@&pagerReqParam=%@",[[NSUserDefaults standardUserDefaults] valueForKey:@"OrgId"],jsonString];
        
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
                                 
                                 [tblViewGuestList reloadData];
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
                     if(guestArray.count > 0)
                     {
                         [guestArray removeAllObjects];
                     }
                     
                     [tblViewGuestList reloadData];
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

@end
