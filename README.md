# ResponsiveLayout1

Practical 1 — **Creating Responsive Android User Interfaces**

The same screen from the practical sheet is built twice, with two different layout
techniques, inside **one** Android Studio project:

| Module | Part | Technique | Application ID |
|---|---|---|---|
| `linear-layout` | Part 1 | nested `LinearLayout`s + `layout_weight` | `com.example.responsivelayout1` |
| `constraint-layout` | Part 2 | `ConstraintLayout` (root **and** inner layouts) + chains with weights | `com.example.responsivelayout1.constraint` |

Both modules produce their own APK, so the two versions can be installed side
by side and compared on the same device.

---

## Opening / building

Open the **root folder** of this repository in Android Studio (it is a single
Gradle project with two application modules) and let it sync, then pick the
run configuration `linear-layout` or `constraint-layout`.

From the command line (JDK 17, Android SDK with platform 35 installed):

```bash
./gradlew assembleDebug
```

Debug APKs:

```
linear-layout/build/outputs/apk/debug/linear-layout-debug.apk
constraint-layout/build/outputs/apk/debug/constraint-layout-debug.apk
```

Toolchain actually used and verified: Gradle **8.9**, Android Gradle Plugin
**8.7.3**, `compileSdk = 35`, `minSdk = 24`, `targetSdk = 34`, Java 17.
Both modules build cleanly (`BUILD SUCCESSFUL`).

---

## The screen

Five horizontal bands, taken from the screenshot in the practical sheet:

| Band | Content | Weight | Background | Text colour |
|---|---|---|---|---|
| 1 | `Lab 1` | **1** | `#000000` | `#FFFFFF` |
| 2 | `Responsive Layout 1` | **1** | `#FFFFFF` | `#000000` |
| 3 | `This` / `is` / `my` / `first` (4 equal cells) | **1** | `#FF0000` / `#0000FF` / `#673AB7` / `#FFFF00` | white, white, white, black |
| 4 | `Android Application` | **2** | `#000000` | `#FFFFFF` |
| 5 | `Change` + `Cancel` buttons | *wrap_content* | Material 3 surface `#FFFBFE` | Material 3 default |

Colours were sampled from the target image rather than guessed, so the palette
matches the sheet exactly (`#673AB7` is the purple cell, not the button purple).
The two buttons are unstyled Material 3 `Button`s, which is why they come out
as `#6750A4` pills with a fully rounded corner — exactly what the sheet shows.

### Why the weights are 1 : 1 : 1 : 2

The four stretchable bands share the height that the `wrap_content` button bar
does not need. Forty of those units go to band 4 and twenty to each of the
first three, i.e. band 4 is exactly **twice** as tall as the others:

```
1 + 1 + 1 + 2 = 5 parts     ->  20 % / 20 % / 20 % / 40 % of the free height
```

That ratio is what the reference screenshot shows, and it is the reason the
layout still looks right when the screen height changes — nothing is tied to a
particular device.

### Part 1 — `LinearLayout`

* `android:orientation="vertical"` on the root, `horizontal` on the coloured row
  and on the button bar.
* Weighted bands use `android:layout_height="0dp"` + `android:layout_weight`
  (`0dp` = "my size comes from my weight, not from my content").
* Weighted cells use `android:layout_width="0dp"` + `layout_weight="1"`, so the
  four quarters are always equal and gap-free.
* `match_parent` is used across the width, `wrap_content` only where the content
  really decides the size (the button bar).
* `baselineAligned="false"` on the coloured row — otherwise the row would align
  text baselines and add height.
* No `px` anywhere; the only fixed values are `dp`/`sp` paddings, margins and
  text sizes, all declared once in `res/values/dimens.xml`.

### Part 2 — `ConstraintLayout`

`ConstraintLayout` has no `layout_weight`, but it has the exact equivalent for
a chain: a view with `layout_height="0dp"` (match-constraint) inside a chain
plus `app:layout_constraintVertical_weight` is sized from the chain's leftover
space.

```xml
app:layout_constraintVertical_weight="1"   <!-- same maths as LinearLayout weight -->
app:layout_constraintTop_toBottomOf="@id/textLabTitle"
app:layout_constraintBottom_toTopOf="@id/colorRow"
```

* The five bands form one **vertical chain**, anchored `parent top` … `parent
  bottom`; the button bar is `wrap_content` and has no weight, so it behaves
  like the unweighted row in Part 1.
* The four cells are a **horizontal chain** with
  `app:layout_constraintHorizontal_weight="1"` each — the ConstraintLayout way
  of writing four equal `weight=1` cells.
* Both inner containers (`colorRow`, `buttonBar`) are `ConstraintLayout`s as
  well, as the sheet requires.
* Every view is placed by constraints only — no absolute positions, no fixed
  widths or heights, so the screen re-flows at any size or orientation.
* Margins are used only where the design needs constant breathing room
  (20 dp around the buttons, 40 dp between them).

---

## Repository / commits

`git log` was built up in the order the work was actually done, so each commit
is one meaningful step rather than one dump:

1. initial project (Gradle wrapper, two modules, manifest, activity, theme)
2. the Part 1 screen: all `TextView`s and the four coloured cells
3. the `Change` / `Cancel` buttons and the final weighted button bar
4. the Part 2 `ConstraintLayout` version of the same screen
5. README

---

## Checklist from the sheet

| Requirement | Where |
|---|---|
| UI recreated with `LinearLayout` | `linear-layout/.../activity_main.xml` |
| `layout_weight` used appropriately | bands 1–4 (1:1:1:2), 4 cells (1:1:1:1) |
| `0dp` used with the weights | every weighted `layout_height` / `layout_width` |
| `match_parent` / `wrap_content` used appropriately | `match_parent` for full-width bands, `wrap_content` for the button bar |
| no hard-coded pixel sizes | only `dp`/`sp` via `dimens.xml` |
| all `TextView`s present | `Lab 1`, `Responsive Layout 1`, `This`, `is`, `my`, `first`, `Android Application` |
| `Change` and `Cancel` present | `@id/buttonChange`, `@id/buttonCancel` |
| same UI recreated with `ConstraintLayout` | `constraint-layout/.../activity_main.xml` |
| views positioned by constraints, relative to parent and to each other | vertical chain + horizontal chain + parent edges |
| UI usable at different screen sizes | all sizes come from weights, `0dp` and constraints |
