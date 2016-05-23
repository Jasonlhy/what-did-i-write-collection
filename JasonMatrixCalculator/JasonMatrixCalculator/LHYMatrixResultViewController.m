//
//  LHYMatrixResultViewController.m
//  JasonMatrixCalculator
//
//  Created by Jason on 3/21/13.
//  Copyright (c) 2013 edu.self. All rights reserved.
//

#import "LHYMatrixResultViewController.h"

@interface LHYMatrixResultViewController ()

@end

@implementation LHYMatrixResultViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    
    // resize the collectionView with the matrix's size for answer
    CGRect frame      = self.collectionView.frame;
    frame.size.width  = resultMatrixCellWidth  * self.resultMatrix.column +  miniWidthSpace * (self.resultMatrix.column - 1);
    frame.size.height = resultMatrixCellHeight * self.resultMatrix.row +  miniHeightSpace * (self.resultMatrix.row - 1);
    
    // "center" the collectionView in program
    double totalWidth = self.view.frame.size.width;
    double newX = (totalWidth - frame.size.width ) /2;
    frame.origin.x = newX;
    self.collectionView.frame = frame;
    

    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    static NSString *cellIdentifier = @"matrixResultCell";
    UICollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:cellIdentifier forIndexPath:indexPath];

    // find the row, column index of matrix
    NSInteger totalItemIndex = indexPath.item;
    NSInteger rowIndex       = totalItemIndex / self.resultMatrix.column;
    NSInteger columnIndex    = totalItemIndex % self.resultMatrix.column;
    NSNumber *number = [self.resultMatrix numberAtRow:rowIndex column:columnIndex];
    
    UILabel *label = (UILabel *)[cell viewWithTag:resultLabelTag];
    label.text = [[NSString alloc] initWithFormat:@"%.1f", [number doubleValue]];
    
    return cell;
}

-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    return self.resultMatrix.row * self.resultMatrix.column;
}

/*
 *
 Handling the event when the user clicks on the back button
 It will "close" this viewController
 *
 */
- (IBAction)backButtonClicked:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}
@end
