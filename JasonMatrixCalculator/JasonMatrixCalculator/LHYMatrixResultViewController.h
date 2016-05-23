//
//  LHYMatrixResultViewController.h
//  JasonMatrixCalculator
//
//  Created by Jason on 3/21/13.
//  Copyright (c) 2013 edu.self. All rights reserved.
//

#define resultLabelTag 200
#define resultMatrixCellWidth 50
#define resultMatrixCellHeight 50

#define miniWidthSpace 10
#define miniHeightSpace 10

#import <UIKit/UIKit.h>
#import "Matrix.h"

@interface LHYMatrixResultViewController : UIViewController<UICollectionViewDataSource>

@property (nonatomic, strong)Matrix *resultMatrix;

#pragma mark - UI Component
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;
- (IBAction)backButtonClicked:(id)sender;

@end
