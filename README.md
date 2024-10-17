[GPT Assistant](https://github.com/adriantache/GPTAssistant) rewrite to target both Android and Desktop (Windows, macOS,
Linux) targets using Kotlin Multiplatform with ComposeUi (with Ktor for network access, Kotlin Serialization for JSON
manipulation and interop with Ktor, DataStore for storing the API key, settings and local message cache).

This is an app that enables interaction with the latest OpenAI ChatGPT, without requiring a monthly subscription. It
requires an API key, which you can obtain ontheir website at https://platform.openai.com/api-keys, after which you must
purchase API credits at https://platform.openai.com/settings/organization/billing/overview (10$ should last you a long
time if you're not using image generation).

An important feature of this app is that it enables the use of Personas, in order to guide the GPT to respond to your
queries taking on a certain persona, and following certain instructions, leading to better results.

In order to make a build of this project, simply open it in IntelliJ IDEA, double press the CTRL key and type:
`gradle packageDistributionForCurrentOS`. This will generate either an MSI or DMG file, depending on whether you are on
Windows or macOS, respectively. The resulting output will be in `\composeApp\build\compose\binaries\main\`.

## Features

### Personas

This app allows you to add and use Personas when making prompts. Personas are instructions for an LLM which give it a
role to play, greatly improving its output. You can add any instructions into a persona, and I encourage you to
experiment with various instructions and tone.
[Some more information](https://ediscoverytoday.com/2024/02/13/the-persona-pattern-in-ai-interactions-artificial-intelligence-best-practices/)
about this pattern.

### Conversation History

All conversations are automatically saved locally for easy reference. On Android 10 or newer, this means that they are
also automatically encrypted.

## FAQ

#### Why don't you use branches?

Since I'm the only person contributing to this project, there's no reason to bother with the (*granted, minor*) overhead
of branches and PRs. All code gets merged to `master` and gets checked by the CI when making a release.

## Pending work

- [ ] Migrate all existing functionality to [AACA](https://adriantache.com/architecture) implementation (to be expanded)
  - [ ] Conversation history
  - [x] Saving conversation
  - [x] Persona selection
  - [x] Storing persona as part of conversation
  - [ ] Getting conversation title from ChatGPT
  - [ ] Settings
    - [ ] Clearing the API key
    - [ ] Disable saving of conversations
    - [ ] Force dark mode?
- [ ] Implement DI (kotlin-inject?)
- [ ] Review all project TODOs
- [ ] Add test coverage for all layers (to be expanded)
- [ ] Align with newest OpenAi changes to text and image generation (to be expanded)
  - [ ] See how we can send images
  - [x] Try to send persona instructions as system messages rather than user messages
    - [ ] Add UI for persona instruction display and improve persona UI
- [ ] Analytics and observability (to be expanded)
- [ ] Fix persona storage
  - [ ] Prevent storing duplicate personas
  - [x] Update personas list when adding/editing/deleting one
  - [x] Fix persona editing
- [ ] Migrations (to be expanded)
- [ ] Rewrite this readme!
- [ ] Better image generation functionality (to be expanded)
- [ ] Sort out platform layer implementation for things like navigation (to be expanded)
- [ ] Error reporting! (and some tests on desktop)

### Nice to have

- cloud backup of conversations
- better UI/UX, animations, etc.
- separate UI for wide desktop windows
- voice input and TTS output + nice UI
- file input, if API allows it
- better handling of markdown text
- selector for AI/image generation provider (maybe integrate something that provides flux.pro)
- automatic/manual conversation tagging/folders
