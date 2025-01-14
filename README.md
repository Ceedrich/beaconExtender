# Beacon Extender

This is a simple server-side mod that allows the range and number of layers of a
beacon to be configured.

## Features

- Set a maximum amount of beacon _layers_ that will improve effects
- Define the _beacon range_ and _effect duration_ to increase either
  **linearly** or **exponentially** with customizable parameters
- Flexible configuration and in-game reloading
- Displays the amount of active layers in the beacon ui

## TODOS

## Configuration

You will find a configuration file `beaconextender.json5` in your usual
`.minecraft/config` folder.

```json5
{
  // Show the number of active layers in the beacon ui
  showBeaconLayers: true,
  // Defines the maximum number of beacon layers that will change the effect
  maxLayers: 6,
  // Defines the method of calculating the effect range
  range: {
    /* Defines the type of the function.
       Can be either "EXPONENTIAL" or "LINEAR".
       The exponential function gets evaluated like
           f(layers) = param1 * param2 ^ layers
       The linear function gets evaluated like
           f(layers) = param1 * layers + param2
    */
    type: "LINEAR",
    // The first parameter of the function.
    param1: 10.0,
    // The second parameter of the function.
    param2: 10.0,
  },
  // Defines the method of calculating the effect duration in seconds
  effectDuration: {
    /* Defines the type of the function.
       Can be either "EXPONENTIAL" or "LINEAR".
       The exponential function gets evaluated like
           f(layers) = param1 * param2 ^ layers
       The linear function gets evaluated like
           f(layers) = param1 * layers + param2
    */
    type: "LINEAR",
    // The first parameter of the function.
    param1: 2.0,
    // The second parameter of the function.
    param2: 9.0,
  },
}
```
