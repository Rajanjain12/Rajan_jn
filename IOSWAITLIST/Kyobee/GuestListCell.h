//
//  GuestListCell.h
//  Kyobee
//
//  Created by Mayur Pandya on 28/03/17.
//
//

#import <UIKit/UIKit.h>

@interface GuestListCell : UITableViewCell

@property (strong, nonatomic) IBOutlet UILabel *lblRank;
@property (strong, nonatomic) IBOutlet UILabel *lblName;
@property (strong, nonatomic) IBOutlet UILabel *lblSeparator;
@property (strong, nonatomic) IBOutlet UIImageView *imgThumb;


@property (strong, nonatomic) IBOutlet NSLayoutConstraint *lblNameLead;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *lblRankLead;

@end
