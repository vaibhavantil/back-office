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
        const { members, setFilter, searchMemberRequest } = this.props;
        return (
            <React.Fragment>
                <Header size="huge">Members</Header>
                <MembersFilter
                    data={members}
                    setFilter={setFilter}
                    search={searchMemberRequest}
                    filterName="Status"
                />
                <MembersList {...this.props} />
            </React.Fragment>
        );
    }
}

Members.propTypes = {
    members: PropTypes.object.isRequired,
    membersRequest: PropTypes.func.isRequired,
    setFilter: PropTypes.func.isRequired,
    searchMemberRequest: PropTypes.func.isRequired
};
