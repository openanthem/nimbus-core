# Contributing to Anthem Open Source projects

Want to hack on changes to the Nimbus Framework?  We have a contributor's guide that explains
[setting up a development environment and the contribution
process](https://anthemopensource.atlassian.net/wiki/spaces/OSS/pages/523436073/Contributing).

This page contains information about reporting issues as well as some tips and
guidelines useful to experienced open source contributors. Finally, make sure
you read our [community guidelines](#anthem-open-source-community-guidelines) before you
start participating.

## Topics

* [Reporting Security Issues](#reporting-security-issues)
* [Reporting Issues](#reporting-other-issues)
* [Quick Contribution Tips and Guidelines](#quick-contribution-tips-and-guidelines)
* [Community Guidelines](#anthem-open-source-community-guidelines)

## Reporting security issues

The Anthem Open Source team take security seriously. If you discover a security
issue, please bring it to their attention right away!

Please **DO NOT** file a public issue, instead send your report privately to
[security@oss.antheminc.com](mailto:security@oss.antheminc.com).

Security reports are greatly appreciated and we will publicly thank you for it.

## Reporting other issues

A great way to contribute to the project is to send a detailed report when you
encounter an issue. We always appreciate a well-written, thorough bug report,
and will thank you for it!

Each Anthem project has its own issue database. 
Before raising an issue, be sure to refer to the respective issue database and
make sure an issue doesn't already include that problem or suggestion.
If you find a match, you can use the "subscribe" button to get notified on
updates. Try to keep each issue terse and poignant.  Do *not* leave random "+1"
or "I have this too" comments, as they only clutter the discussion, and don't
help resolving it. However, if you have ways to reproduce the issue or have
additional information that may help resolving the issue, please leave a comment.

Also include the steps required to reproduce the problem if possible and
applicable. This information will help us review and fix your issue faster.
When sending lengthy log-files, consider posting them as a gist (https://gist.github.com).
Don't forget to remove sensitive data from your logfiles before posting (you can
replace those parts with "REDACTED").

When reporting issues, always refer to the project readme's issue reporting section for
specific details and requirements for issue reporting.

## Quick contribution tips and guidelines

This section gives the experienced contributor some tips and guidelines.

There is a formal process for contributing. We try to keep our contribution process simple so you’ll want to contribute frequently.

### The basic contribution workflow
![Simple process](oss.platform/assets/simple-contribution.png)

Step 1 is always to be sure there isn't already an issue in the repo. If you can't find one, go ahead and create one, then request to be assigned to it. If you do find one, and no one is assigned to it, then request to be assigned to it. When you're assigned to the issue, your pull requests for it will be reviewed and considered.

All Anthem repositories have code and documentation. You use this same workflow for either content type. For example, you can find and fix doc or code issues. Also, you can propose a new project feature or propose a new feature tutorial by contributing a Markdown file that contains a good description of the proposal.

### Pull requests are always welcome

Not sure if that typo is worth a pull request? Found a bug and know how to fix
it? Do it! We will appreciate it. Any significant improvement should be
documented as a GitHub issue within the project's issue database.

We are always thrilled to receive pull requests. We do our best to process them
quickly. If your pull request is not accepted on the first try,
don't get discouraged! The review process we use is meant to be receptive and fair.

### Talking to other Anthem Open Source users and contributors

<table class="tg">
  <col width="45%">
  <col width="65%">
  <tr>
    <td>Forums</td>
    <td>
      A public forum for users to discuss questions and explore current design patterns and
      best practices projects in the Anthem Open Source Ecosystem. Join the 
      conversations at <a href="http://discourse.oss.antheminc.com/" target="_blank">http://discourse.oss.antheminc.com/</a>.
    </td>
  </tr>  
  <tr>
    <td>Stack Overflow</td>
    <td>
      To be announced later.
    </td>
  </tr>
</table>


### Conventions

Fork the repository and make changes on your fork in a feature branch:

- If it's a bug fix branch, name it XXXX-something where XXXX is the number of
    the issue.
- If it's a feature branch, create an enhancement issue to announce
    your intentions, and name it XXXX-something where XXXX is the number of the
    issue.

Submit unit tests for your changes.  Refer to the project readme's unit tests section for
project details on unit testing.

Pull request titles should include the GitHub issue ID from the project's issue database
that supports the change.

Pull request descriptions should be as clear as possible and include a reference
to all the issues that they address.

Commit messages must start with a capitalized and short summary (max. 50 chars)
written in the imperative, followed by an optional, more detailed explanatory
text which is separated from the summary by an empty line.

Code review comments may be added to your pull request. Discuss, then make the
suggested modifications and push additional commits to your feature branch. Post
a comment after pushing. New commits show up in the pull request automatically,
but the reviewers are notified only when you comment.

Pull requests must be cleanly rebased on top of develop without multiple branches
mixed into the PR.

**Git tip**: If your PR no longer merges cleanly, use `rebase master` in your
feature branch to update your pull request rather than `merge master`.

Before you make a pull request, squash your commits into logical units of work
using `git rebase -i` and `git push -f`. A logical unit of work is a consistent
set of patches that should be reviewed together: for example, upgrading the
version of a vendored dependency and taking advantage of its now available new
feature constitute two separate units of work. Implementing a new function and
calling it in another file constitute a single logical unit of work. The very
high majority of submissions should have a single commit, so if in doubt: squash
down to one.

After every commit, make sure the test suite passes. Include documentation
changes in the same pull request so that a revert would remove all traces of
the feature or fix.

Include an issue reference like `Closes #XXXX` or `Fixes #XXXX` in the pull request
description that close an issue. Including references automatically closes the issue
on a merge.

Please see the [Coding Style](https://anthemopensource.atlassian.net/wiki/spaces/OSS/pages/523698209/Nimbus+Framework+Code+Style) for further guidelines.

### Merge approval

Anthem will outline this process soon.

### How can I become a maintainer?

As the platform grows, maintainers will become the backbone of our projects.
Each project will be organically grown through its maintainers. However, at this
time while, as we work through details, Anthem will maintain this project with
dedicated employees.

## Anthem Open Source community guidelines

We want to keep the Anthem Open Source community growing and collaborative. We need
your help to keep it that way. To help with this we've come up with some general
guidelines for the community as a whole:

* Be nice: Be courteous, respectful and polite to fellow community members:
  no regional, racial, gender, or other abuse will be tolerated. We like
  nice people way better than mean ones!

* Encourage diversity and participation: Make everyone in our community feel
  welcome, regardless of their background and the extent of their
  contributions, and do everything possible to encourage participation in
  our community.

* Keep it legal: Basically, don't get us in trouble. Share only content that
  you own, do not share private or sensitive information, and don't break
  the law.

* Stay on topic: Make sure that you are posting to the correct channel and
  avoid off-topic discussions. Remember when you update an issue or respond
  to an email you are potentially sending to a large number of people. Please
  consider this before you update. Also remember that nobody likes spam.

* Don't send email to the maintainers: There's no need to send email to the
  maintainers to ask them to investigate an issue or to take a look at a
  pull request. Instead of sending an email, GitHub mentions should be
  used to ping maintainers to review a pull request, a proposal or an
  issue.

### Guideline violations — 3 strikes method

The point of this section is not to find opportunities to punish people, but we
do need a fair way to deal with people who are making challenging, and
intolerable.

1. First occurrence: We'll give you a friendly, but public reminder that the
   behavior is inappropriate according to our guidelines.

2. Second occurrence: We will send you a private message with a warning that
   any additional violations will result in removal from the community.

3. Third occurrence: Depending on the violation, we may need to delete or ban
   your account.

**Notes:**

* Obvious spammers are banned on first occurrence. If we don't do this, we'll
  have spam all over the place.

* Violations are forgiven after 6 months of good behavior, and we won't hold a
  grudge.

* People who commit minor infractions will get some education, rather than
  hammering them in the 3 strikes process.

* The rules apply equally to everyone in the community, no matter how much
    you've contributed.

* Extreme violations of a threatening, abusive, destructive or illegal nature
    will be addressed immediately and are not subject to 3 strikes or forgiveness.

* Contact abuse@oss.antheminc.com to report abuse or appeal violations. In the case of
    appeals, we know that mistakes happen, and we'll work with you to come up with a
    fair solution if there has been a misunderstanding.
