# Reference

Moved out of the README; nothing was removed.

> The current install path is `pam composer require pushinbr/pam-native-live-activities` followed by `pam doctor --fix`; `pam add` below is the legacy alias.

## Install

```bash
pam add live-activities
pam doctor
```

The generated iOS project includes the ActivityKit extension and the Android build includes the ongoing-notification implementation.

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

