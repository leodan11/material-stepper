# Android Material Stepper

[![](https://jitpack.io/v/leodan11/material-stepper.svg)](https://jitpack.io/#leodan11/material-stepper)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)

This library allows to use Material steppers inside Android applications.

Quoting the [documentation](https://www.google.com/design/spec/components/steppers.html):

>Steppers display progress through a sequence by breaking it up into multiple logical and numbered steps.

# Credits

This is just an updated version of [Android Material Stepper](https://github.com/stepstone-tech/android-material-stepper) and applying some of the active pull requests in it. 
Credits go completely to its creator and the people who have contributed with those pull requests.

## Supported features
  - color customisation of individual widgets inside of the stepper via View attributes or a style from a theme
  - custom texts of individual widgets inside of the stepper via View attributes or a style from a theme
  - embedding the stepper anywhere in the view hierarchy and changing the stepper type for various device configurations, e.g. phone/tablet, portrait/landscape
  - step validation
  - use with Fragments or Views
  - showing errors in tabs
  - showing stepper feedback for ongoing operations (see [Stepper feedback](https://material.io/guidelines/components/steppers.html#steppers-types-of-steppers))
  
## Getting started

### Download (from JCenter)
```groovy
implementation 'com.github.leodan11:material-stepper:$tag'
```

### Create layout in XML

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.github.leodan11.stepper.StepperLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/stepperLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:ms_stepperType="progress_bar" />
```

For a complete list of StepperLayout attributes see [StepperLayout attributes](#StepperLayout attributes).

### Create step Fragment(s)
Step fragments must extend [Fragment](https://developer.android.com/reference/androidx/fragment/app/Fragment.html)
and implement `com.github.leodan11.stepper.Step`

```java
public class StepFragmentSample extends Fragment implements Step {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step, container, false);

        //initialize your UI

        return v;
    }

    @Override
    public VerificationError verifyStep() {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null;
    }

    @Override
    public void onSelected() {
        //update UI when selected
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
    }
    
}
```

### Extend AbstractFragmentStepAdapter
AbstractFragmentStepAdapter extends [FragmentPagerAdapter](https://developer.android.com/reference/androidx/fragment/app/FragmentPagerAdapter)
but instead of overriding the method `getItem(int)` you must override the `createStep(int)` method.

```java
public static class MyStepperAdapter extends AbstractFragmentStepAdapter {

    public MyStepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        final StepFragmentSample step = new StepFragmentSample();
        Bundle b = new Bundle();
        b.putInt(CURRENT_STEP_POSITION_KEY, position);
        step.setArguments(b);
        return step;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        return new StepViewModel.Builder(context)
                .setTitle(R.string.tab_title) //can be a CharSequence instead
                .create();
    }
}

```

### Set adapter in Activity

```java
public class StepperActivity extends AppCompatActivity {

    private StepperLayout mStepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new MyStepperAdapter(getSupportFragmentManager(), this));
    }
}
```

### Add a StepperListener in the Activity (optional)
```java
public class StepperActivity extends AppCompatActivity implements StepperLayout.StepperListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //...
        mStepperLayout.setListener(this);
    }

    @Override
    public void onCompleted(View completeButton) {
        Toast.makeText(this, "onCompleted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(VerificationError verificationError) {
        Toast.makeText(this, "onError! -> " + verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {
        Toast.makeText(this, "onStepSelected! -> " + newStepPosition, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReturn() {
        finish();
    }

}
```

## StepperLayout attributes

### View attributes
A list of base StepperLayout attributes used for behaviour configuration & base UI configuration.
For advanced styling please see [StepperLayout style attributes](#stepperlayout-style-attributes).

| Attribute name                  | Format                                                              | Description |
| --------------------------------|---------------------------------------------------------------------|-------------|
| *ms_stepperType*                | one of `dots`, `progress_bar`, `tabs` or `none`                     | **REQUIRED:** Type of the stepper |
| *ms_backButtonColor*            | color or reference                                                  | BACK button's text color, can be also set via `StepperLayout#setBackButtonColor(int)`/`StepperLayout#setBackButtonColor(ColorStateList)`           |
| *ms_nextButtonColor*            | color or reference                                                  | NEXT button's text color, can be also set via `StepperLayout#setNextButtonColor(int)`/`StepperLayout#setNextButtonColor(ColorStateList)`            |
| *ms_completeButtonColor*        | color or reference                                                  | COMPLETE button's text color, can be also set via `StepperLayout#setCompleteButtonColor(int)`/`StepperLayout#setCompleteButtonColor(ColorStateList)`            |
| *ms_activeStepColor*            | color or reference                                                  | Active step's color            |
| *ms_inactiveStepColor*          | color or reference                                                  | Inactive step's color            |
| *ms_bottomNavigationBackground* | reference                                                           | Background of the bottom navigation            |
| *ms_backButtonBackground*       | reference                                                           | BACK button's background            |
| *ms_nextButtonBackground*       | reference                                                           | NEXT button's background            |
| *ms_completeButtonBackground*   | reference                                                           | COMPLETE button's background            |
| *ms_backButtonText*             | string or reference                                                 | BACK button's text            |
| *ms_nextButtonText*             | string or reference                                                 | NEXT button's text            |
| *ms_completeButtonText*         | string or reference                                                 | COMPLETE button's text            |
| *ms_tabStepDividerWidth*        | dimension or reference                                              | The width of the horizontal tab divider used in tabs stepper type            |
| *ms_showBackButtonOnFirstStep*  | boolean                                                             | Flag indicating if the Back (Previous step) button should be shown on the first step. False by default.            |
| *ms_errorColor*                 | color or reference                                                  | Error color in Tabs stepper |
| *ms_showErrorStateEnabled*      | boolean                                                             | Flag indicating whether to show the error state. Only applicable for 'tabs' type. False by default. |
| *ms_showErrorStateOnBackEnabled*| boolean                                                             | Flag indicating whether to keep showing the error state when user moves back. Only applicable for 'tabs' type. False by default. |
| *ms_tabNavigationEnabled*       | boolean                                                             | Flag indicating whether step navigation is possible by clicking on the tabs directly. Only applicable for 'tabs' type. True by default. |
| *ms_stepperFeedbackType*        | flag(s): `none` or `tabs`, `content_progress`, `content_fade`, `content_overlay`, `disabled_bottom_navigation` & `disabled_content_interaction` | Type(s) of stepper feedback. Can be a combination of `tabs`, `content_progress`, `content_fade`, `content_overlay`, `disabled_bottom_navigation` & `disabled_content_interaction`. Default is `none`.|
| *ms_stepperFeedback_contentFadeAlpha* | float                                                         | An alpha value from 0 to 1.0f to be used for the faded out view if `content_fade` stepper feedback type is set. 0.5f by default. |
| *ms_stepperFeedback_contentOverlayBackground* | reference                                             | Background to be used for the overlay on top of the content if `content_overlay` stepper feedback type is set. |
| *ms_showBottomNavigation*       | boolean                                                             | Flag indicating if the Bottom Navigation bar should be shown on the layout. True by default. |
| *ms_stepperLayoutTheme*         | reference                                                           | Theme to use for even more custom styling of the stepper layout. It is recommended that it should extend @style/MSDefaultStepperLayoutTheme, which is the default theme used. |

### StepperLayout style attributes
A list of `ms_stepperLayoutTheme` attributes responsible for styling of StepperLayout's child views.

| Attribute name                    | Description                                                   |
| ----------------------------------|---------------------------------------------------------------|
| *ms_bottomNavigationStyle*        | Used by ms_bottomNavigation in layout/ms_stepper_layout       |
| *ms_tabsContainerStyle*           | Used by ms_stepTabsContainer in layout/ms_stepper_layout      |
| *ms_backNavigationButtonStyle*    | Used by ms_stepPrevButton in layout/ms_stepper_layout         |
| *ms_nextNavigationButtonStyle*    | Used by ms_stepNextButton in layout/ms_stepper_layout         |
| *ms_completeNavigationButtonStyle*| Used by ms_stepCompleteButton in layout/ms_stepper_layout     |
| *ms_colorableProgressBarStyle*    | Used by ms_stepProgressBar in layout/ms_stepper_layout        |
| *ms_stepPagerProgressBarStyle*    | Used by ms_stepPagerProgressBar in layout/ms_stepper_layout   |
| *ms_stepPagerOverlayStyle*        | Used by ms_stepPagerOverlay in layout/ms_stepper_layout   |
| *ms_stepTabsScrollViewStyle*      | Used by ms_stepTabsScrollView in layout/ms_tabs_container     |
| *ms_stepTabsInnerContainerStyle*  | Used by ms_stepTabsInnerContainer in layout/ms_tabs_container |
| *ms_stepTabsProgressMessageStyle* | Used by ms_stepTabsProgressMessage in layout/ms_tabs_container|
| *ms_stepTabContainerStyle*        | Used in layout/ms_step_tab_container                          |
| *ms_stepTabNumberStyle*           | Used by ms_stepNumber in layout/ms_step_tab                   |
| *ms_stepTabDoneIndicatorStyle*    | Used by ms_stepDoneIndicator in layout/ms_step_tab            |
| *ms_stepTabIconBackgroundStyle*   | Used by ms_stepIconBackground in layout/ms_step_tab           |
| *ms_stepTabTitleStyle*            | Used by ms_stepTitle in layout/ms_step_tab                    |
| *ms_stepTabSubtitleStyle*         | Used by ms_stepSubtitle in layout/ms_step_tab                    |
| *ms_stepTabDividerStyle*          | Used by ms_stepDivider in layout/ms_step_tab                  |


