import React from 'react';
import { Link } from 'react-router-dom';
import {
    Button,
    Card,
    Dimmer,
    Loader,
    Segment,
    Message
} from 'semantic-ui-react';
import AssetCard from '../asset-card/AssetCard';
import Pagination from '../pagination/Pagination';
import Fliter from '../list-filter/AssetsListFilter';
import { filterList } from 'app/lib/helpers';

class AssetList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeList: [],
            filteredList: [],
            activeFilter: 'ALL'
        };
        this.onChangePage = this.onChangePage.bind(this);
        this.filterChangeHandler = this.filterChangeHandler.bind(this);
        this.assetUpdateHandler = this.assetUpdateHandler.bind(this);
        this.pollingHandler = this.pollingHandler.bind(this);
    }

    onChangePage(activeList) {
        this.setState({ activeList });
    }

    assetUpdateHandler(id, value) {
        this.props.assetUpdate(id, value);
        this.filterChangeHandler(null, { value: this.state.activeFilter });
    }

    filterChangeHandler(e, { value }) {
        this.setState(
            {
                activeFilter: value
            },
            () => {
                const filteredList = filterList(
                    this.state.activeFilter,
                    this.props.assetsList
                );
                this.setState({
                    filteredList
                });
            }
        );
    }

    pollingHandler() {
        const { polling: { pollStart, pollStop, polling }, token } = this.props;
        if (polling) {
            pollStop(token, 2000);
        } else {
            pollStart(token, 2000);
        }
    }

    componentWillUnmount() {
        const { polling: { pollStop, polling } } = this.props;
        if (polling) {
            pollStop();
        }
    }

    render() {
        const {
            assetsList,
            errors,
            updateStatus,
            polling: { polling }
        } = this.props;
        const { activeList, filteredList, activeFilter } = this.state;
        const items = activeFilter === 'ALL' ? assetsList : filteredList;
        return (
            <Segment className="assets-list">
                <Dimmer active={assetsList && !assetsList.length} inverted>
                    <Loader size="large">Loading</Loader>
                </Dimmer>
                {errors && errors.length ? (
                    <AssetListErrors errors={errors} />
                ) : (
                    <div>
                        <Fliter
                            list={assetsList}
                            activeFilter={this.state.activeFilter}
                            filterChange={this.filterChangeHandler}
                            polling={polling}
                            pollingHandler={this.pollingHandler}
                        />
                        <Card.Group itemsPerRow={4}>
                            {assetsList && !!activeList.length ? (
                                activeList.map(asset => (
                                    <AssetCard
                                        key={asset.id}
                                        asset={asset}
                                        assetUpdate={this.assetUpdateHandler}
                                        updateStatus={updateStatus}
                                    />
                                ))
                            ) : (
                                <p className="filter__message">
                                    No items by this filter. Select other.
                                </p>
                            )}
                        </Card.Group>
                        <Pagination
                            items={items}
                            onChangePage={this.onChangePage}
                        />
                    </div>
                )}
            </Segment>
        );
    }
}

const AssetListErrors = errors => {
    return errors.errors.map((err, id) => (
        <Message negative key={id}>
            <p>
                {err.message}.
                {(err.status === 403 || err.status === 401) && (
                    <span>
                        Go to <Link to="/login">login</Link>
                    </span>
                )}
            </p>
        </Message>
    ));
};

export default AssetList;
