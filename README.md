# PAM Native Live Activities

ActivityKit Live Activities and Dynamic Island presentation on iOS 16.2+, with an Android ongoing-notification equivalent. Supports start, update, end, deep links, progress and ActivityKit push tokens returned by `active()`.

Remote ActivityKit updates require APNs token-based delivery from your server. Android 13+ requires notification permission. Avoid high-frequency updates unless the displayed value materially changes.

## Install

```bash
composer require pushinbr/pam-native-live-activities
pam mobile prepare
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
