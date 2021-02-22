---
name: Bug report
about: Broken or unintended behavior with one of our libraries.
title: ''
labels: bug-unconfirmed, question
assignees: ''



## Library
<!-- ⚠️ Please try the latest published version and fill in your exact version number below e.g. `msal@2.7.0`. ⚠️ -->
- [ ] `msal@1.x.x` or `@azure/msal@1.x.x`
- [ ] `@azure/msal-browser@2.x.x`
- [ ] `@azure/msal-node@1.x.x`
- [ ] `@azure/msal-react@1.x.x`
- [ ] `@azure/msal-angular@0.x.x`
- [X] `@azure/msal-angular@1.x.x`
- [ ] `@azure/msal-angular@2.x.x`
- [ ] `@azure/msal-angularjs@1.x.x`

## Framework
<!-- ⚠️ If using a framework please specify which version you are using e.g. Angular 11 or React 17 ⚠️ -->
- [X] Angular
- [ ] React
- [ ] Other

## Description

We have multiple websites running under the same domain when switching between these websites the token is not refreshed as MSALGuard does not seem to validate the Aud claim and that it matches the ClientId. In our particular environment we hold roles in the token, we go to Website-A and the token will hold the roles for the required user and application in the token, however when we switch to Website-B the token is not refreshed and is still that for Website-A with its roles. We have a workaround by creating a new gaurd which checks the toekns aud claim against the clientid of the app and if not the same issues a login refreshing the toekn so it matches that for the website.



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

## MSAL Should Check Aud Claim of Token

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

- [N] 
Version:

## Security

- [Y] Is this issue security related?

## Source

- [ ] Internal (Microsoft)
- [ ] Customer request
