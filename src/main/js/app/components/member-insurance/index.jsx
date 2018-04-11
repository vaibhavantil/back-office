import React from 'react';
import PropTypes from 'prop-types';
import { Header } from 'semantic-ui-react';
// import MembersFilter from '../members/members-filter/MembersFilter';
import MemberInsuranceList from './member-insurance-list/MemberInsuranceList';

export default class MemberInsurance extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        const { memberInsurance, memberInsRequest } = this.props;
        if (!memberInsurance.list.length) {
            memberInsRequest();
        }
    }

    render() {
        return (
            <React.Fragment>
                <Header size="huge"> Member Insurance </Header>
                {/* <MembersFilter {...this.props} /> */}
                <MemberInsuranceList {...this.props} />
            </React.Fragment>
        );
    }
}
MemberInsurance.propTypes = {
    memberInsurance: PropTypes.object.isRequired,
    memberInsRequest: PropTypes.func.isRequired
};
