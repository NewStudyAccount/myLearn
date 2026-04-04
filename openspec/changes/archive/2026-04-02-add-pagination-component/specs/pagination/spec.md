## ADDED Requirements

### Requirement: Pagination component renders correctly
The system SHALL provide a Pagination component that wraps Element Plus ElPagination and renders correctly with all standard features.

#### Scenario: Basic rendering
- **WHEN** Pagination component is mounted with total of 100 items
- **THEN** component displays page numbers, current page indicator, and navigation controls

### Requirement: Pagination supports v-model binding
The system SHALL support two-way binding for current page and page size via v-model.

#### Scenario: Page change via v-model
- **WHEN** user clicks on page number 3
- **THEN** v-model bound currentPage updates to 3
- **AND** component emits 'update:currentPage' event with value 3

#### Scenario: Page size change via v-model
- **WHEN** user selects page size 20 from dropdown
- **THEN** v-model bound pageSize updates to 20
- **AND** component emits 'update:pageSize' event with value 20

### Requirement: Pagination emits standard events
The system SHALL emit standard pagination events when user interacts with the component.

#### Scenario: Size change event
- **WHEN** user changes page size
- **THEN** component emits 'size-change' event with new page size value

#### Scenario: Current page change event
- **WHEN** user navigates to different page
- **THEN** component emits 'current-change' event with new page number

#### Scenario: Previous page event
- **WHEN** user clicks previous page button
- **THEN** component emits 'prev-click' event with previous page number

#### Scenario: Next page event
- **WHEN** user clicks next page button
- **THEN** component emits 'next-click' event with next page number

### Requirement: Pagination supports custom layout
The system SHALL allow customization of pagination layout elements.

#### Scenario: Custom layout configuration
- **WHEN** component is configured with layout="total, prev, pager, next"
- **THEN** only total count, previous, pager, and next buttons are displayed

#### Scenario: Default page sizes
- **WHEN** component is mounted without pageSizes prop
- **THEN** default page sizes [10, 20, 50, 100] are available in selector
