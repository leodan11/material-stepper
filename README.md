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

### Download (from JitPack)

Add the JitPack repository to your `settings.gradle`:

```groovy
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Then add the dependency:

```groovy
implementation 'com.github.leodan11:material-stepper:$tag'
```

`$tag` is a placeholder for the latest release tag; the JitPack badge at the top of this
README shows the current one. Replace it before syncing, for example with `2.0.0`.

### Requirements

- `minSdk 21`
- AndroidX and a Material Components theme (`Theme.MaterialComponents.*` or `Theme.Material3.*`)
- `compileSdk 36` and JDK 17 to build the project from source

### Migrating from 1.x to 2.0.0

- The step container is now `ViewPager2`.
- `StepAdapter#getPagerAdapter()` returns a `RecyclerView.Adapter<?>`.
- `AbstractStepAdapter` extends `RecyclerView.Adapter`; subclasses implement
  `onCreateViewHolder` and `onBindViewHolder` in addition to `createStep`. The root view
  of each View-based step must fill the pager (`match_parent`), otherwise `ViewPager2`
  throws `IllegalStateException("Pages must fill the whole ViewPager2")`.
- `AbstractFragmentStepAdapter` extends `FragmentStateAdapter`. Steps beyond the
  offscreen page limit may be destroyed and recreated, so step fragments must save
  their state properly (fields are not retained indefinitely).
- `StepperLayout#setPageTransformer` now accepts a `ViewPager2.PageTransformer`.
- The RTL-specific page transformer was removed because ViewPager2 handles RTL natively.
- The default colors are now resolved from the app theme (`?attr/colorPrimary`,
  `?attr/colorOnSurface`, `?attr/colorError` and `?attr/colorSurface`) instead of the
  hardcoded blue/grey/red palette, so the stepper follows Material 2, Material 3 and
  their dark variants automatically. The previous colors are only used as a fallback
  when the theme does not define those attributes. See
  [Theming (Material 2 / Material 3)](#theming-material-2--material-3).
- The step number and the done indicator are now drawn in black or white, picked from
  the luminance of the step circle. The color defined in `ms_stepTabNumberStyle` and
  `ms_stepTabDoneIndicatorStyle` is ignored; only their layout, size and typography
  attributes are used.

`StepperListener#onStepSelected` now has a default implementation, so overriding it is
optional.

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

For a complete list of StepperLayout attributes see [StepperLayout attributes](#stepperlayout-attributes).

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

`AbstractFragmentStepAdapter` extends [FragmentStateAdapter](https://developer.android.com/reference/androidx/viewpager2/adapter/FragmentStateAdapter),
but instead of overriding `createFragment(int)` you must override `createStep(int)`.

Prefer the `(FragmentActivity)` or `(Fragment)` constructors: they resolve the
`FragmentManager` and the lifecycle automatically. The `(Context, FragmentManager)`
constructor is still available for advanced cases, but the context must implement
`LifecycleOwner` or wrap one.

```java
public static class MyStepperAdapter extends AbstractFragmentStepAdapter {

    public MyStepperAdapter(@NonNull FragmentActivity activity) {
        super(activity);
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
        // `context` is still a protected field of AbstractFragmentStepAdapter
        return new StepViewModel.Builder(context)
                .setTitle(R.string.tab_title) // can be a CharSequence instead
                .create();
    }
}
```

### Extend AbstractStepAdapter (View-based steps)

For plain Views, `AbstractStepAdapter` now extends `RecyclerView.Adapter`. In addition
to `createStep(int)`, implement `onCreateViewHolder` and `onBindViewHolder`; the root
view of each step must fill the pager.

```java
public static class MyViewStepAdapter extends AbstractStepAdapter {

    private final List<MyStep> steps;

    public MyViewStepAdapter(@NonNull Context context, @NonNull List<MyStep> steps) {
        super(context);
        this.steps = steps;
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public Step createStep(int position) {
        return steps.get(position);
    }

    @Override
    public Step findStep(int position) {
        return steps.get(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step, parent, false);
        return new RecyclerView.ViewHolder(view) { };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bind `steps.get(position)` into holder.itemView
    }
}
```

`MyStep` is your model implementing `Step`; the step views are created and recycled by
the adapter, so keep any state outside the view holder.

### Set adapter in Activity

```java
public class StepperActivity extends AppCompatActivity {

    private StepperLayout mStepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new MyStepperAdapter(this));
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
| *ms_showErrorMessageEnabled*    | boolean                                                             | Flag indicating whether to show the error message below the tab title. Only applicable for 'tabs' type. False by default. |
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

## Theming (Material 2 / Material 3)

The library does not ship a fixed color palette. By default every color is resolved
from the app theme, so the stepper works with `Theme.MaterialComponents.*` (Material 2)
and `Theme.Material3.*` (Material 3), in both light and dark mode:

| Element | Theme attribute |
| ------------------------------------ | --------------------------------------- |
| active step, progress bar, navigation buttons | `?attr/colorPrimary`           |
| inactive steps, dividers, disabled navigation buttons | `?attr/colorOnSurface` (low opacity) |
| step titles, subtitles, progress message | `?attr/colorOnSurface`             |
| tabs container and bottom navigation background | `?attr/colorSurface`           |
| error state | `?attr/colorError` |

A Material Components theme is required, because the internal styles extend
`Widget.Material3.Button.TextButton`, which itself extends the Material 2 chain.

The step number and the done indicator are always drawn in black or white, picked at
runtime from the luminance of the step circle (composited over the surface color first,
so translucent inactive circles are measured correctly). `ms_stepTabNumberStyle` and
`ms_stepTabDoneIndicatorStyle` still control the layout, size and typography of the
marker (the done indicator also keeps its `srcCompat` drawable), but their color is
ignored. This keeps the marker readable for any `ms_activeStepColor`,
`ms_inactiveStepColor` or `ms_errorColor`, in light and dark themes.

### Overriding the colors

The `ms_*` attributes still win over the defaults:

```xml
<com.github.leodan11.stepper.StepperLayout
    app:ms_activeStepColor="?attr/colorPrimary"
    app:ms_errorColor="?attr/colorError"
    app:ms_stepperType="tabs" />
```

### Overriding the styles

For deeper customization define a theme that extends `MSDefaultStepperLayoutTheme` and
point `ms_stepperLayoutTheme` to it. The theme is applied as an overlay on top of the
app theme, so `?attr/...` references keep working:

```xml
<style name="MyStepper" parent="MSDefaultStepperLayoutTheme">
    <item name="ms_tabsContainerStyle">@style/MyTabsContainer</item>
    <item name="ms_stepTabTitleStyle">@style/MyStepTabTitle</item>
</style>
```

> Avoid Material 3 only attributes such as `?attr/colorSurfaceContainer` or
> `?attr/colorOutline`: they are undefined in Material 2 themes and the affected style
> item is silently ignored. Use `?attr/colorSurface` and `?attr/colorOnSurface` instead.

## License

MIT. See [LICENSE](LICENSE).


