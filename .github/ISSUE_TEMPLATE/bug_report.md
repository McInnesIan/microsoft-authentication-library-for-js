---
name: Bug report
about: Broken or unintended behavior with one of our libraries.
title: ''
labels: bug-unconfirmed, question
assignees: ''

---

<!-- Before posting your issue please check if your question is answered in one of the following places: 

    * msal-browser@2.x FAQ: https://github.com/AzureAD/microsoft-authentication-library-for-js/blob/dev/lib/msal-browser/FAQ.md
    * msal@1.x FAQ: https://github.com/AzureAD/microsoft-authentication-library-for-js/blob/dev/lib/msal-core/docs/FAQ.md

    Please follow the issue template below. Failure to do so will result in a delay in answering your question.
-->

## Library
<!-- ⚠️ Please try the latest published version and fill in your exact version number below e.g. `msal@2.7.0`. ⚠️ -->
- [ ] `msal@1.x.x` or `@azure/msal@1.x.x`
- [] `@azure/msal-browser@2.x.x`
- [ ] `@azure/msal-node@1.x.x`
- [ ] `@azure/msal-react@1.x.x`
- [ ] `@azure/msal-angular@0.x.x`
- [1.1.2] `@azure/msal-angular@1.x.x`
- [ ] `@azure/msal-angular@2.x.x`
- [ ] `@azure/msal-angularjs@1.x.x`

## Framework
<!-- ⚠️ If using a framework please specify which version you are using e.g. Angular 11 or React 17 ⚠️ -->
- [11] Angular
- [ ] React
- [ ] Other

## Description

Two applications runng with same domain under SSO with diffent ClinetIds and using roles from the token. When we switch between the two websites the token is not refreshed to that of the current site, MSLGuard is not validating the audiance (aud) of the token. If we run Website-A the token is fetched and contains the correct aud (clientid of the website) if we then switch to Website-B it still uses the token for Website-A containing the wrong clientid and roles. This can be fixed by checking the the token aud matches the configured clientid and if they do not match issueing a login using MSALService (which because they are logged-in using SSO) fetches the correct toekn. MSALGuard should check the token audiance (aud) aginst the configured clientid and perform this action.

## Error Message
<!-- Please provide the error message, stack trace and/or logs here-->

## MSAL Configuration

```js
// Provide configuration values here.
// For Azure B2C issues, please include your policies.
```

## Reproduction steps

```js
// Provide relevant code snippets here.
// For Azure B2C issues, please include your policies.
```

## Expected behavior

## Identity Provider

- [X] Azure AD
- [ ] Azure B2C Basic Policy
- [ ] Azure B2C Custom Policy
- [ ] ADFS
- [ ] Other

## Browsers/Environment

- [X] Chrome
- [ ] Firefox
- [ ] Edge
- [ ] Safari
- [ ] IE
- [ ] Other (Please add browser name here)

## Regression

- [ ] Did this behavior work before?
Version:

## Security

- [ ] Is this issue security related?

## Source

- [ ] Internal (Microsoft)
- [ ] Customer request
