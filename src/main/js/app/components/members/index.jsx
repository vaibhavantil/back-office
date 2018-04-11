import React from 'react';
import PropTypes from 'prop-types';
import { Header } from 'semantic-ui-react';
import MembersList from './members-list/MembersList';
import MembersFilter from './members-filter/MembersFilter';

export default class Members extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        const { members, membersRequest } = this.props;
        if (!members.list.length) {
            membersRequest();
        }
    }

    render() {
        return (
            <React.Fragment>
                <Header size="huge">Members</Header>
                <MembersFilter {...this.props} />
                <MembersList {...this.props} />
            </React.Fragment>
        );
    }
}

Members.propTypes = {
    members: PropTypes.object.isRequired,
    membersRequest: PropTypes.func.isRequired
};
