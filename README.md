# Beacon Extender

This is a simple server-side mod that allows the range and number of layers of a
beacon to be configured.

## Features

- Set a maximum amount of beacon layers that will improve effects
- Define the beacon range to increase either **linearly** or **exponentially**
  with customizable parameters
- Flexible configuration and in-game reloading with the `/reload` command

## TODOS

- Add custom functions for the effect duration

## Configuration

You will find a configuration file `beaconextender.toml` in your usual
`.minecraft/config` folder.

```toml
# Defines the maximum number of layers a beacon can have.
# Any more layers will not have any effect on the beacon
# Default: 6
maxLayers = 6

[range]
# Defines the method of calculating the beacon range
# The type can be either "exponential" or "linear" (default "linear")
# This requires [range.exponential] or [range.linear] to be set respectivley.
# Default: exponential
type = "exponential"

# Determines the parameters of the exponential method given by the function
#    range(n) = initialValue * base^n
# where n is the number of layers of the beacon
# Default:
# [range.exponential]
# initialValue = 12.0
# base = 1.5
[range.exponential]
initialValue = 12.0
base = 1.5

# Determines the parameters of the linear method given by the function
#     range(n) = (slope * n) + offset
# where n is the number of layers of the beacon
# Default:
# [range.linear]
# slope = 10.0
# offset = 10.0
[range.linear]
slope = 10.0
offset = 10.0
```
