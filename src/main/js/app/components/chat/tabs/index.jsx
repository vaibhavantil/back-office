import React from "react";
import PropTypes from "prop-types";
import { Tab } from "semantic-ui-react";
import styled from "styled-components";
import ChatTab from "./ChatTab";
import DetailsTab from "./DetailsTab";
import ClaimsTab from "./ClaimsTab";
import InsuranceTab from "./InsuranceTab";
import InsuranceListTab from "./InsuranceListTab";
import PaymentsTab from './PaymentsTab';

const TabContainer = styled(Tab.Pane)`
  &&& {
    display: flex;
    flex-direction: column;
    min-width: 700px;
    height: ${props => (props.isChatTab ? "100%" : "auto")};
    margin-bottom: 50px !important;
  }
`;

const TabItem = ({ props, TabContent, isChatTab }) => (
  <TabContainer isChatTab={isChatTab}>
    <TabContent {...props} />
  </TabContainer>
);

TabItem.propTypes = {
  props: PropTypes.object.isRequired,
  TabContent: PropTypes.func,
  isChatTab: PropTypes.bool
};

/* eslint-disable react/display-name */
const memberPagePanes = (props, addMessage, socket) => {
  const { insurance } = props;
  const panes = [
    {
      menuItem: "Details",
      render: () => <TabItem props={props} TabContent={DetailsTab} />
    },
    {
      menuItem: "Chat",
      render: () => (
        <TabItem
          TabContent={ChatTab}
          props={{ ...props, addMessage, socket }}
          isChatTab={true}
        />
      )
    },
    {
      menuItem: "Claims",
      render: () => <TabItem props={props} TabContent={ClaimsTab} />
    }
  ];
  if (!insurance.error.length && insurance.data) {
    panes.push({
      menuItem: "Current Insurance",
      render: () => <TabItem props={props} TabContent={InsuranceTab} />
    });
  }
  if (!insurance.error.length && insurance.data) {
    panes.push({
      menuItem: "All Insurances",
      render: () => <TabItem props={props} TabContent={InsuranceListTab} />
    });
  }
  if (!insurance.error.length && insurance.data) {
    panes.push({
      menuItem: "Payments",
      render: () => <TabItem props={props} TabContent={PaymentsTab} />
    });
  }
  return panes;
};
export default memberPagePanes;
