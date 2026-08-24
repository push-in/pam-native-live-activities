<!-- pam:product-page:start -->
<div align="center">

# PAM Native Live Activities

**Keep time-sensitive experiences visible beyond the app window.**

Drive ActivityKit Live Activities and Android ongoing live notifications from one typed state model.

[![Latest version](https://img.shields.io/packagist/v/pushinbr/pam-native-live-activities?style=flat-square&label=stable)](https://packagist.org/packages/pushinbr/pam-native-live-activities)
[![CI](https://img.shields.io/github/actions/workflow/status/push-in/pam-native-live-activities/ci.yml?branch=main&style=flat-square&label=CI)](https://github.com/push-in/pam-native-live-activities/actions)
![PHP](https://img.shields.io/badge/PHP-8.5-777BB4?style=flat-square&logo=php&logoColor=white)
![Android](https://img.shields.io/badge/Android-API%2026%2B-3DDC84?style=flat-square&logo=android&logoColor=white)
![iOS](https://img.shields.io/badge/iOS-15%2B-000000?style=flat-square&logo=apple&logoColor=white)

**[Documentation](https://push-in.github.io/pam-docs/native/overview/) · [Quick start](#quick-start) · [What you can build](#what-you-can-build) · [PAM ecosystem](https://push-in.github.io/pam-docs/ecosystem/) · [Issues](https://github.com/push-in/pam-native-live-activities/issues)**

</div>

---

## Why PAM Native Live Activities

Drive ActivityKit Live Activities and Android ongoing live notifications from one typed state model. The public API is strictly typed for PHP 8.5; expensive or frame-sensitive work stays in Rust or the platform SDK instead of crossing the application boundary every frame.

| | |
| --- | --- |
| **Best for** | A focused capability you can add to any PAM Native application |
| **Native path** | ActivityKit · Android ongoing notifications |
| **Application model** | Composer package + generated native integration |
| **Design rule** | Independent module; no feed, vertical, or application template bundled |

## What you can build

- Sports scores and live events
- Delivery, trip, and queue progress
- Timers, workouts, and session status

## Quick start

Already have a PAM Native project? Add only this capability:

```bash
pam composer require pushinbr/pam-native-live-activities
pam doctor --fix
```

New to PAM? Follow the **[five-minute PAM Native setup](https://push-in.github.io/pam-docs/native/overview/)** once, then return here. Your application stays a normal Composer project with a committed lockfile.
<!-- pam:product-page:end -->

## See it in action

ActivityKit Live Activities and Dynamic Island presentation on iOS 16.2+, with an Android ongoing-notification equivalent. Supports start, update, end, deep links, progress and ActivityKit push tokens returned by `active()`.

Remote ActivityKit updates require APNs token-based delivery from your server. Android 13+ requires notification permission. Avoid high-frequency updates unless the displayed value materially changes.

## Install

```bash
pam add live-activities
pam doctor
```

The generated iOS project includes the ActivityKit extension and the Android build includes the ongoing-notification implementation.

## Start and update an activity

```php
use Pam\Native\LiveActivities\LiveActivities;
use Pam\Native\LiveActivities\LiveActivityContent;

$activities = new LiveActivities();
$content = new LiveActivityContent('Order on the way', 'Preparing', 0.15, 'myapp://orders/42');

$activities->start('order-42', $content, function (?string $identifier, ?string $error) use ($activities): void {
    if ($identifier === null) {
        return;
    }

    $activities->update(
        $identifier,
        new LiveActivityContent('Order on the way', 'Courier assigned', 0.45),
        static fn (?string $updated, ?string $error) => null,
    );
}, pushEnabled: true);
```

Store the returned identifier for later updates and termination. Use `active()` after process restart to reconcile the operating-system state.


## What installation does

`pam add live-activities` resolves the official compatible package, performs a non-mutating Composer preflight, updates the normal `composer.json` and `composer.lock`, refreshes generated native integration when required, and leaves the project ready for `pam doctor` validation.

Use `pam packages` to inspect availability and `pam remove live-activities` to uninstall the capability safely. Direct Composer commands are an advanced interoperability path; PAM is the supported application workflow.

## API guide

| API | Responsibility |
| --- | --- |
| `LiveActivities` | Start, update, list, and end live activities. |
| `LiveActivityContent` | Define bounded title, status, progress, and deep-link content. |

All coded states, kinds, and variants are sequential integer-backed enums. Use enum cases in application code; do not depend on raw wire numbers.

## Production checklist

- Persist returned identifiers and reconcile them with `active()` after restart.
- Send remote ActivityKit updates through authenticated APNs token delivery.
- Update only when displayed information materially changes.
- Run `pam doctor`, `pam test`, and a signed release build on every supported platform.
- Exercise denial, cancellation, backgrounding, process restart, and offline behavior before release.

## Troubleshooting

- **Start fails on iOS:** verify ActivityKit capability, extension, and iOS version.
- **Android activity is absent:** request notification permission on Android 13+.
- **Updates target nothing:** reconcile the identifier with current OS state.
- **Native integration is stale:** run `pam doctor --fix`, rebuild the native host, and inspect the first reported diagnostic.

## Compatibility and support

This package targets PAM Native `0.8.x`, Android API 26+, and iOS 15+ unless a platform-specific section above states a stricter requirement. Platform SDKs, credentials, entitlements, physical hardware, and store configuration remain application responsibilities.

- [PAM documentation](https://push-in.github.io/pam-docs/introduction/)
- [PAM Native overview](https://push-in.github.io/pam-docs/native/overview/)
- [Plugin and native capability model](https://push-in.github.io/pam-docs/native/plugins/)
- [Report an issue](https://github.com/push-in/pam-native-live-activities/issues)

Security vulnerabilities should be reported through the repository security policy or GitHub private vulnerability reporting, not a public issue.
