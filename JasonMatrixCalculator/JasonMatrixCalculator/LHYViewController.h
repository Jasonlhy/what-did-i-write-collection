//
//  LHYViewController.h
//  JasonMatrixCalculator
//
//  Created by Jason on 3/20/13.
//  Copyright (c) 2013 edu.self. All rights reserved.
//

#import <UIKit/UIKit.h>

#define defaultSize 2
#define matrixCellWidth 50
#define matrixCellHeight 30
#define textFieldTag 100
#define centerAfterMove 80

#define miniWidthSpace 10
#define miniHeightSpace 10

#define addIndex 0
#define subIndex 1
#define multiplyIndex 2
#define invIndex 3

@interface LHYViewController : UIViewController <UICollectionViewDataSource, UICollectionViewDelegate, UITextFieldDelegate>

#pragma mark - UI Components
@property (weak, nonatomic) IBOutlet UICollectionView *topMatrix;
@property (weak, nonatomic) IBOutlet UICollectionView *buttomMatrix;
@property (weak, nonatomic) IBOutlet UIStepper *stepper;
@property (weak, nonatomic) IBOutlet UISegmentedControl *segmentedControl;

#pragma mark - Actions from UI
- (IBAction)overViewButtonClicked:(UIButton *)sender;
- (IBAction)stepperValueIsChanged:(UIStepper *)sender;
- (IBAction)resetButtonClicked:(UIButton *)sender;
- (IBAction)calculateButtonClicked:(id)sender;
- (IBAction)changeOperator:(UISegmentedControl *)sender;


@end
