# Task List for Thursday Jan 23, 2020

1. Generate the .d.ts for the Vue global events component
    * Not as useful as I thought it would be
1. Review conversion of JavaScript to TypeScript
    * Conversion of existing (vs. regen and copy guts) seems to require:
        * tsconfig.json
        * package.json:
            * dependencies:
                * "vue-class-component": "^7.0.2",
                * "vue-property-decorator": "^8.3.0",
            * devDependencies
                *     "@types/jest": "^24.0.19",
                      "@vue/cli-plugin-typescript": "^4.1.0",
                      "@vue/eslint-config-typescript": "^4.0.0",
                      "typescript": "~3.5.3",

1. Componentize the websocket stuff. Goals:
    * Stop duplicating code
    * Reduce open websockets to 1
    * Move to Vue events
1. Make it easier to reference cards by a unique key instead
   of its title, which can change

