# User Messages Documentation:

This document captures all hardcoded and resource-based strings shown to the user via Dialogs, Toasts, and Overlays.

## 1. Connection & Network Status
These messages appear during connection setup or when network quality fluctuates.

| Type                | Trigger Event          | Raw English Message                                                                                              |
| :------------------ | :--------------------- | :--------------------------------------------------------------------------------------------------------------- |
| **Warning (Toast)** | Metered Connection     | `"Warning: Your active network connection is metered!"`                                                          |
| **Alert (Overlay)** | Poor Connection        | `"Poor server connection — gameplay may be affected and your session could disconnect."`                         |
| **Alert (Overlay)** | Slow Connection        | `"Your connection is slow and may affect gameplay performance; please switch to a stable network and retry."`      |
| **Alert (Toast)**   | Connection Optimized   | `"Your streaming is optimized and running at [X] Mbps"`                                                          |
| **Error (Dialog)**  | Port Blocked           | `"Your device's current network connection is blocking. Streaming over the Internet may not work while connected to this network."` |
| **Error (Dialog)**  | Connection Failed      | `"Failed to start [Stage Name] (error [Code])"`                                                                  |
| **Error (Dialog)**  | Firewall/Ports         | `"Check your firewall and port forwarding rules for port(s): TCP [Audio] UDP [Video] UDP [Control]"`               |
| **Error (Dialog)**  | Poor Connection (30s)  | **Title:** `"Connection Issue"` <br> **Body:** `"Connection remained poor for 30 seconds.\n\nPlease go back and try again."` |
| **Error (Dialog)**  | Generic Termination    | **Title:** `"Connection Terminated"` <br> **Body:** `"The connection was terminated"`                             |

## 2. Hardware & Decoder Errors
Messages related to device compatibility and video decoding failures.

| Type               | Trigger Event      | Raw English Message                                                                                                                              |
| :----------------- | :----------------- | :----------------------------------------------------------------------------------------------------------------------------------------------- |
| **Error (Dialog)** | Missing AVC        | `"This device or ROM doesn't support hardware accelerated H.264 playback."`                                                                      |
| **Error (Toast)**  | HDR Init Fail      | `"Display does not support HDR10"`                                                                                                               |
| **Error (Toast)**  | Android Ver Fail   | `"HDR requires Android 7.0 or later"`                                                                                                            |
| **Error (Toast)**  | Main10 Fail        | `"Decoder does not support HEVC Main10HDR10"`                                                                                                    |
| **Error (Toast)**  | HEVC Fallback      | `"No HEVC decoder found.\nFalling back to H.264."`                                                                                               |
| **Error (Toast)**  | AV1 Missing        | `"No AV1 decoder found"`                                                                                                                         |
| **Error (Toast)**  | Video Init Fail    | `"Video decoder failed to initialize. Your device may not support the selected resolution or frame rate."`                                       |
| **Error (Dialog)** | Encoding Error     | `"The host PC reported a fatal video encoding error.\n\nTry disabling HDR mode, changing the streaming resolution, or changing your host PC's display resolution."` |
| **Error (Dialog)** | No Video Traffic   | `"No video received from host."`                                                                                                                 |
| **Error (Dialog)** | Frame Drop Issue   | `"Your network connection isn't performing well. Reduce your video bitrate setting or try a faster connection."`                                 |

## 3. Session & Interaction Alerts
General application state and session management messages.

| Type                   | Trigger Event        | Raw English Message                                                                                                                                  |
| :--------------------- | :------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Confirmation**       | Back/Quit Pressed    | **Title:** `"Are you sure you want to quit?"` <br> **Body:** `"Unsaved progress may be lost if the game is not properly exited and synced with the store."` |
| **Alert (Dialog)**     | Internet Lost        | **Title:** `"No internet connection"` <br> **Body:** `"You are not connected to internet. Please connect to the internet and try again."`              |
| **Notification**       | Subscription Alert   | `"Your subscription pack will exhaust in [X] min"`                                                                                                   |
| **Success (Toast)**    | OSC Reset            | `"On-screen controls reset to default"`                                                                                                              |
| **Alert (Dialog)**     | Logout/Restart       | **Title:** `"Successfully quit"` <br> **Body:** `"you have successfully logged out"`                                                                   |
| **Tooltip**            | Control Shortcuts    | `"Press & hold start button for 3 sec to enable mouse emulation. Select+A to display a keyboard on the screen. Select+X to display settings."`         |
| **Toast**              | Keyboard Shortcut    | `"Select+A to show/hide a keyboard."`                                                                                                                |

## 4. Dynamic / Backend Messages
The following messages are populated at runtime via server responses:

*   **Exit Message (`exitResponse`):** Specific error details fetched from `ExitMessageService` based on termination codes.
*   **Maintenance Alert:** `"Our servers will be shutting down in 60 seconds due to scheduled maintenance. Please save your game progress."`
*   **Time Left:** `"You have [X] minutes of gameplay remaining! Click on Top-up to buy a streaming plan."`
